package com.totem.food.application.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class HeaderMissingExceptionTest {

    @Test
    void testConstructorWithMessage() {

        //## Given - Mock
        String errorMessage = "Header is missing";

        //## When
        var exception = new HeaderMissingException(errorMessage);

        //## Then
        assertEquals(errorMessage, exception.getMessage());
    }

}