package com.innowise.microservice.exception;

public class CardNumberExistsException extends RuntimeException {
    public CardNumberExistsException(String cardNumber) {
        super("Card with number " + cardNumber + " already exists");
    }
}
