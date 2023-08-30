package com.totem.food.application.ports.in.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {

    @Test
    void hash256() {
        final var expected = "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3";
        final var hashed = assertDoesNotThrow(() -> Utils.hash256("123"));
        assertEquals(expected, hashed);
    }
}