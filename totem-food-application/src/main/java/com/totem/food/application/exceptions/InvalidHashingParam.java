package com.totem.food.application.exceptions;

public class InvalidHashingParam extends RuntimeException {

    public InvalidHashingParam(String message){
        super(message);
    }
}