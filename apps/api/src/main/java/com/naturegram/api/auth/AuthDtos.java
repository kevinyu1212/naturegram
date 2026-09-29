package com.naturegram.api.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;

public final class AuthDtos {

    private AuthDtos() {
    }

    public record SignupRequest(
            @NotBlank
            @Pattern(regexp = "^[A-Za-z0-9_]{3,32}$")
            String username,
            @NotBlank
            @Email
            @Size(max = 320)
            String email,
            @NotBlank
            @Size(min = 12, max = 128)
            String password) {

    }

    public record LoginRequest(
            @NotBlank
            @Size(max = 320)
            String usernameOrEmail,
            @NotBlank
            String password) {

    }

    public record UserResponse(UUID id, String username, String email, UserRole role, Instant createdAt) {

        static UserResponse from(UserAccount user) {
            return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getCreatedAt());
        }
    }

    public record CsrfResponse(String token) {

    }

    public record ApiError(String code, String message) {

    }
}
