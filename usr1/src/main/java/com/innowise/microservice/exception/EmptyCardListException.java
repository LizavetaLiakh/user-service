package com.innowise.microservice.exception;

public class EmptyCardListException extends RuntimeException {
    public EmptyCardListException(Iterable<Long> ids) {
        super("No cards found with ids: " + ids);
    }
}
