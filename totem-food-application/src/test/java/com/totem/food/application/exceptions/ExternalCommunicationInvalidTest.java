package com.totem.food.application.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExternalCommunicationInvalidTest {

    @Test
    void checkMessage(){
        final var exception = new ExternalCommunicationInvalid("Message");
        assertEquals("Message", exception.getMessage());
    }

}