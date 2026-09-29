package com.naturegram.api.auth;

public class DuplicateAccountException extends RuntimeException {

    public DuplicateAccountException() {
        super("Username or email is already registered.");
    }
}
