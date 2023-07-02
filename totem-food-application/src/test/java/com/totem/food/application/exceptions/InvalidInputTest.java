package com.totem.food.application.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidInputTest {

    @Test
    void checkMessage(){
        final var exception = new InvalidInput("Message");
        assertEquals("Message", exception.getMessage());
    }

}