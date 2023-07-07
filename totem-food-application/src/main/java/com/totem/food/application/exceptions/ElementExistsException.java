package com.totem.food.application.exceptions;

public class ElementExistsException extends RuntimeException {

    public ElementExistsException(String message){
        super(message);
    }
}
