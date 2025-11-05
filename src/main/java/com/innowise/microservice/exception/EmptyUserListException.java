package com.innowise.microservice.exception;

public class EmptyUserListException extends RuntimeException {
    public EmptyUserListException(Iterable<Long> ids) {
        super("No users found with ids: " + ids);
    }
}
