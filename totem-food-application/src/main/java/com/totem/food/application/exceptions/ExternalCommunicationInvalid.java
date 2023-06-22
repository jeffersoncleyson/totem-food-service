package com.totem.food.application.exceptions;

public class ExternalCommunicationInvalid extends RuntimeException {

    public ExternalCommunicationInvalid(String message){
        super(message);
    }
}
