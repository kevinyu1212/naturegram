package com.naturegram.api.auth;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler({DuplicateAccountException.class, DataIntegrityViolationException.class})
    ResponseEntity<AuthDtos.ApiError> accountConflict() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new AuthDtos.ApiError("ACCOUNT_CONFLICT", "Username or email is already registered."));
    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<AuthDtos.ApiError> invalidCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthDtos.ApiError("INVALID_CREDENTIALS", "Username/email or password is incorrect."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<AuthDtos.ApiError> invalidRequest() {
        return ResponseEntity.badRequest()
                .body(new AuthDtos.ApiError("VALIDATION_FAILED", "Request fields are invalid."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<AuthDtos.ApiError> invalidArgument() {
        return ResponseEntity.badRequest()
                .body(new AuthDtos.ApiError("INVALID_REQUEST", "Request fields are invalid."));
    }
}
