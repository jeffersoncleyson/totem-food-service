package com.totem.food.domain.customer;

import com.totem.food.domain.exceptions.InvalidDomainField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import static com.totem.food.domain.customer.CustomerDomain.MAX_CUSTOMER_LENGTH;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CustomerDomain")
class CustomerDomainTest {

    @Test
    void fillDatesWhenIdIsNotEmpty() {

        //## Given
        var customer = new CustomerDomain();
        customer.setId(UUID.randomUUID().toString());
        customer.setName("John");

        //## When
        customer.fillDates();

        //## Then
        assertNull(customer.getCreateAt());
        assertNull(customer.getModifiedAt());
    }

    @Test
    void fillDatesWhenIdIsEmpty() {

        //## Given
        var customer = new CustomerDomain();
        customer.fillDates();

        //## When
        var now = ZonedDateTime.now(ZoneOffset.UTC);

        //## Then
        assertEquals(now.getZone(), customer.getCreateAt().getZone());
        assertEquals(now.getZone(), customer.getModifiedAt().getZone());
        assertTrue(customer.getCreateAt().isEqual(now) || customer.getCreateAt().isBefore(now));
        assertTrue(customer.getModifiedAt().isEqual(now) || customer.getModifiedAt().isBefore(now));
    }

    @Test
    void updateModifiedAt() {

        //## Given
        var customer = new CustomerDomain();
        customer.updateModifiedAt();
        customer.setModifiedAt(ZonedDateTime.now().minusHours(1));

        //## When
        var actualModifiedAt = ZonedDateTime.now(ZoneOffset.UTC);

        //## Then
        assertTrue(actualModifiedAt.isAfter(customer.getModifiedAt()));
    }

    @Test
    void validateCustomerWhenNameLengthIsLessThanOrEqualToMax() {

        //## Given
        var customer = new CustomerDomain();
        customer.setName("John");

        //## When
        customer.validateName();

        //## Then
        assertDoesNotThrow(customer::validateName);
    }

    @Test
    void validateCustomerWhenNameLengthIsGreaterThanMaxThenThrowException() {

        //## Given
        var customer = new CustomerDomain();
        customer.setName("Este é um nome de cliente muito longo que excede o comprimento máximo. " +
                "é simplesmente uma simulação de texto da indústria tipográfica e de impressos, " +
                "e vem sendo utilizado desde o século XVI");

        //## When
        var exception = assertThrows(InvalidDomainField.class, customer::validateName);

        //## Then
        assertEquals(exception.getMessage(), "Max length accepted is ".concat(String.valueOf(MAX_CUSTOMER_LENGTH)));
    }

    @Test
    void validateNameWhenNameIsEmpty() {

        //## Given
        var customer = new CustomerDomain();

        //## When
        customer.setName("");

        //## Then
        assertDoesNotThrow(customer::validateName);
    }

    @Test
    void validEmailAddressWhenEmailIsCorrect() {

        //## Given
        var customer = new CustomerDomain();

        //## When
        customer.setEmail("john.doe@example.com");

        //## Then
        assertDoesNotThrow(customer::validEmailAddress);
    }

    @Test
    void validEmailAddressWhenEmailIsIncorrectThenThrowException() {

        //## Given
        CustomerDomain customer = new CustomerDomain();
        customer.setEmail("invalidemailblabla.com.com");

        //## When
        var exception = assertThrows(InvalidDomainField.class, customer::validEmailAddress);

        //## Then
        assertEquals("Invalid e-mail address.", exception.getMessage());
    }
}