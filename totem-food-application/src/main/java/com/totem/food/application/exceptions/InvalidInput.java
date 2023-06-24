package com.totem.food.application.exceptions;

public class InvalidInput extends RuntimeException {

    public InvalidInput(String message){
        super(message);
    }
}
