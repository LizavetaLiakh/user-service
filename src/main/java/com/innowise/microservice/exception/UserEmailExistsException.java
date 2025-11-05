package com.innowise.microservice.exception;

public class UserEmailExistsException extends RuntimeException {
    public UserEmailExistsException(String email) {
        super("User with email " + email + " already exists");
    }
}
