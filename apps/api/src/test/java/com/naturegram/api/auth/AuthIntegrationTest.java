package com.naturegram.api.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class AuthIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> DATABASE = new PostgreSQLContainer<>(
            DockerImageName.parse("postgis/postgis:16-3.4").asCompatibleSubstituteFor("postgres"))
            .withDatabaseName("naturegram")
            .withUsername("naturegram")
            .withPassword("test-password");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", DATABASE::getJdbcUrl);
        registry.add("spring.datasource.username", DATABASE::getUsername);
        registry.add("spring.datasource.password", DATABASE::getPassword);
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserAccountRepository users;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void clearUsers() {
        jdbcTemplate.update("DELETE FROM observations");
        jdbcTemplate.update("DELETE FROM taxa");
        users.deleteAll();
    }

    @Test
    void signupLoginMeAndLogoutUseRevocableSession() throws Exception {
        Csrf csrf = csrf();
        mvc.perform(withCsrf(post("/api/v1/auth/signup"), csrf)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {"username":"Nature_1","email":"NATURE@example.org","password":"correct-horse-battery"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("nature_1"))
                .andExpect(jsonPath("$.email").value("nature@example.org"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.passwordHash").doesNotExist());

        UserAccount account = users.findByUsernameIgnoreCase("nature_1").orElseThrow();
        assertThat(account.getPasswordHash()).isNotEqualTo("correct-horse-battery");
        assertThat(passwordEncoder.matches("correct-horse-battery", account.getPasswordHash())).isTrue();

        MvcResult login = mvc.perform(withCsrf(post("/api/v1/auth/login"), csrf)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {"usernameOrEmail":"NATURE@EXAMPLE.ORG","password":"correct-horse-battery"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(account.getId().toString()))
                .andReturn();

        MockHttpSession session = (MockHttpSession) login.getRequest().getSession(false);
        assertThat(session).isNotNull();
        mvc.perform(get("/api/v1/auth/me").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("nature_1"));

        Csrf postLoginCsrf = csrf();
        mvc.perform(withCsrf(post("/api/v1/auth/logout").session(session), postLoginCsrf))
                .andExpect(status().isNoContent());
        assertThat(session.isInvalid()).isTrue();
        mvc.perform(get("/api/v1/auth/me")).andExpect(status().isUnauthorized());
    }

    @Test
    void signupRequiresCsrfAndRejectsDuplicateAccounts() throws Exception {
        String request = """
                {"username":"field_user","email":"field@example.org","password":"correct-horse-battery"}
                """;
        mvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isForbidden());

        Csrf csrf = csrf();
        mvc.perform(withCsrf(post("/api/v1/auth/signup"), csrf)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated());

        mvc.perform(withCsrf(post("/api/v1/auth/signup"), csrf)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {"username":"FIELD_USER","email":"other@example.org","password":"correct-horse-battery"}
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("ACCOUNT_CONFLICT"));
    }

    @Test
    void signupValidatesPasswordLengthAndLoginHidesCredentialFailures() throws Exception {
        Csrf csrf = csrf();
        mvc.perform(withCsrf(post("/api/v1/auth/signup"), csrf)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {"username":"short_pass","email":"short@example.org","password":"short"}
                                """))
                .andExpect(status().isBadRequest());

        String maxPassword = "a".repeat(72);
        mvc.perform(withCsrf(post("/api/v1/auth/signup"), csrf)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {"username":"long_pass","email":"long@example.org","password":"%s"}
                                """.formatted(maxPassword)))
                .andExpect(status().isCreated());

        mvc.perform(withCsrf(post("/api/v1/auth/login"), csrf)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {"usernameOrEmail":"long_pass","password":"%sx"}
                                """.formatted(maxPassword)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("INVALID_CREDENTIALS"));

        mvc.perform(withCsrf(post("/api/v1/auth/login"), csrf)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                                {"usernameOrEmail":"missing@example.org","password":"wrong-password"}
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("INVALID_CREDENTIALS"));
    }

    @Test
    void observationsArePrivateByDefaultAndScopedToOwner() throws Exception {
        UserAccount owner = users.save(UserAccount.create(
                "obs_owner", "owner@example.org", passwordEncoder.encode("test-password")));
        users.save(UserAccount.create(
                "obs_other", "other@example.org", passwordEncoder.encode("test-password")));

        Csrf csrf = csrf();
        MvcResult created = mvc.perform(withCsrf(
                        post("/api/v1/observations")
                                .with(user("obs_owner").roles("USER")),
                        csrf)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"observedAt":"2026-09-30T10:15:00Z","title":"Field observation"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.observerId").value(owner.getId().toString()))
                .andExpect(jsonPath("$.visibility").value("private"))
                .andExpect(jsonPath("$.location").doesNotExist())
                .andReturn();

        String observationId = objectMapper.readTree(
                created.getResponse().getContentAsString()).get("id").asText();

        mvc.perform(get("/api/v1/observations/" + observationId)
                        .with(user("obs_owner").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(observationId));

        mvc.perform(get("/api/v1/observations/" + observationId)
                        .with(user("obs_other").roles("USER")))
                .andExpect(status().isNotFound());

        mvc.perform(get("/api/v1/observations")
                        .with(user("obs_other").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void observationEndpointsRequireAuthenticationAndValidateRequiredFields() throws Exception {
        mvc.perform(get("/api/v1/observations"))
                .andExpect(status().isUnauthorized());

        Csrf csrf = csrf();
        mvc.perform(withCsrf(
                        post("/api/v1/observations")
                                .with(user("validation_user").roles("USER")),
                        csrf)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));
    }
    @Test
    void flywayEnablesPostgisExtension() {
        String version = jdbcTemplate.queryForObject("SELECT PostGIS_Version()", String.class);
        assertThat(version).isNotBlank();
    }

    private Csrf csrf() throws Exception {
        MvcResult result = mvc.perform(get("/api/v1/auth/csrf"))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        Cookie cookie = result.getResponse().getCookie("XSRF-TOKEN");
        assertThat(cookie).isNotNull();
        return new Csrf(cookie, body.get("token").asText());
    }

    private MockHttpServletRequestBuilder withCsrf(MockHttpServletRequestBuilder request, Csrf csrf) {
        return request.cookie(csrf.cookie()).header("X-XSRF-TOKEN", csrf.token());
    }

    private record Csrf(Cookie cookie, String token) {

    }
}
