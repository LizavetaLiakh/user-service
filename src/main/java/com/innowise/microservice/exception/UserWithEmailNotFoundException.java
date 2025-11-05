package com.innowise.microservice.exception;

public class UserWithEmailNotFoundException extends RuntimeException {
    public UserWithEmailNotFoundException(String email) {
        super("User with email " + email + " not found");
    }
}
