package com.totem.food.application.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidHashingParamTest{

    @Test
    void checkMessage(){
        final var exception = new InvalidHashingParam("Message");
        assertEquals("Message", exception.getMessage());
    }

}