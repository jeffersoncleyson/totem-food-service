package com.totem.food.domain.category;

import com.totem.food.domain.exceptions.InvalidDomainField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import static com.totem.food.domain.category.CategoryDomain.MAX_CATEGORY_LENGTH;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("CategoryDomain")
class CategoryDomainTest {

    @Test
    void fillDatesWhenIdIsNotEmpty() {

        //## Given
        var category = CategoryDomain.builder()
                .id(UUID.randomUUID().toString())
                .name("Bebida")
                .build();

        //## When
        category.fillDates();

        //## Then
        assertNull(category.getCreateAt());
        assertNull(category.getModifiedAt());
    }

    @Test
    void fillDatesWhenIdIsEmpty() {

        //## Given
        var category = CategoryDomain.builder().build();
        category.fillDates();

        //## When
        var now = ZonedDateTime.now(ZoneOffset.UTC);

        //## Then
        assertEquals(now.getZone(), category.getCreateAt().getZone());
        assertEquals(now.getZone(), category.getModifiedAt().getZone());
        assertTrue(category.getCreateAt().isEqual(now) || category.getCreateAt().isBefore(now));
        assertTrue(category.getModifiedAt().isEqual(now) || category.getModifiedAt().isBefore(now));
    }

    @Test
    void updateModifiedAt() {

        //## Given
        var category = new CategoryDomain();
        category.updateModifiedAt();
        category.setModifiedAt(ZonedDateTime.now().minusHours(1));

        //## When
        var actualModifiedAt = ZonedDateTime.now(ZoneOffset.UTC);

        //## Then
        assertTrue(actualModifiedAt.isAfter(category.getModifiedAt()));
    }

    @Test
    void validateCategoryWhenNameLengthIsLessThanOrEqualToMax() {

        //## Given
        var category = CategoryDomain.builder()
                .name("Acompanhamento")
                .build();

        //## When
        category.validateCategory();

        //## Then
        assertDoesNotThrow(category::validateCategory);
    }

    @Test
    void validateCategoryWhenNameLengthIsGreaterThanMaxThenThrowException() {

        //## Given
        var category = CategoryDomain.builder()
                .name("Este é um nome de categoria muito longo que excede o comprimento máximo")
                .build();

        //## When
        var exception = assertThrows(InvalidDomainField.class, category::validateCategory);

        //## Then
        assertEquals(exception.getMessage(), "Max length accepted is ".concat(String.valueOf(MAX_CATEGORY_LENGTH)));
    }
}