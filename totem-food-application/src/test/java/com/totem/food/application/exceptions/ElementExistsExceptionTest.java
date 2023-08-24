package com.totem.food.application.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ElementExistsExceptionTest {

    @Test
    void checkMessage(){
        final var exception = new ElementExistsException("Message");
        assertEquals("Message", exception.getMessage());
    }

}