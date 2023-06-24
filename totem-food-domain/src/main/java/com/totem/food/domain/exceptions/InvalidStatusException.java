package com.totem.food.domain.exceptions;

public class InvalidStatusException extends RuntimeException {

    public InvalidStatusException(String domain, String status, String expected){
        super(String.format("Invalid %s status [%s] expected to be [%s]", domain, status, expected));
    }
}