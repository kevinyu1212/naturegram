package com.naturegram.api.auth;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserAccountRepository users;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserAccountRepository users,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public UserAccount signup(AuthDtos.SignupRequest request) {
        String username = request.username().toLowerCase(Locale.ROOT);
        String email = request.email().strip().toLowerCase(Locale.ROOT);
        if (request.password().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException("Password must not exceed 72 UTF-8 bytes.");
        }
        if (users.existsByUsernameIgnoreCase(username) || users.existsByEmailIgnoreCase(email)) {
            throw new DuplicateAccountException();
        }
        return users.save(UserAccount.create(username, email, passwordEncoder.encode(request.password())));
    }

    public Authentication authenticate(AuthDtos.LoginRequest request) {
        if (request.password().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new BadCredentialsException("Invalid credentials.");
        }
        String identifier = request.usernameOrEmail().strip();
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(identifier, request.password()));
    }

    @Transactional(readOnly = true)
    public UserAccount findByUsername(String username) {
        return users.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalStateException("Authenticated account no longer exists."));
    }
}
