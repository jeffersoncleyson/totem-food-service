package com.totem.food.application.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ElementNotFoundExceptionTest {

    @Test
    void checkMessage(){
        final var exception = new ElementNotFoundException("Message");
        assertEquals("Message", exception.getMessage());
    }

}