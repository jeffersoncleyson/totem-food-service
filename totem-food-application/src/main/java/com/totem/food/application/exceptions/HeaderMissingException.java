package com.totem.food.application.exceptions;

public class HeaderMissingException extends RuntimeException {

    public HeaderMissingException(String message) {
        super(message);
    }
}
