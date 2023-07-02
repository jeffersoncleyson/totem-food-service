package com.totem.food.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ProductDomain")
class ProductDomainTest {

    @Test
    void updateModifiedAt() {

        //## Given
        var product = new ProductDomain();
        product.updateModifiedAt();
        product.setModifiedAt(ZonedDateTime.now().minusHours(1));

        //## When
        var actualModifiedAt = ZonedDateTime.now(ZoneOffset.UTC);

        //## Then
        assertTrue(actualModifiedAt.isAfter(product.getModifiedAt()));
    }


    @Test
    void fillDatesWhenIdIsNotEmpty() {

        //## Given
        var product = new ProductDomain();
        product.setId(UUID.randomUUID().toString());
        product.setName("Coca-Cola");

        //## When
        product.fillDates();

        //## Then
        assertNull(product.getCreateAt());
        assertNull(product.getModifiedAt());
    }

    @Test
    void fillDatesWhenIdIsEmpty() {

        //## Given
        var product = new ProductDomain();
        product.fillDates();

        //## When
        var now = ZonedDateTime.now(ZoneOffset.UTC);

        //## Then
        assertEquals(now.getZone(), product.getCreateAt().getZone());
        assertEquals(now.getZone(), product.getModifiedAt().getZone());
        assertTrue(product.getCreateAt().isEqual(now) || product.getCreateAt().isBefore(now));
        assertTrue(product.getModifiedAt().isEqual(now) || product.getModifiedAt().isBefore(now));
    }
}