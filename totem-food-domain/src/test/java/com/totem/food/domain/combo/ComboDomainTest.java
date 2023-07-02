package com.totem.food.domain.combo;

import com.totem.food.domain.product.ProductDomain;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ComboDomainTest {

    @Test
    void updateModifiedAt() {

        //## Given
        final var modifiedAt = ZonedDateTime.now(ZoneOffset.UTC);
        final var comboDomain = new ComboDomain(
                UUID.randomUUID().toString(),
                "name",
                20D,
                List.of(new ProductDomain()),
                modifiedAt,
                ZonedDateTime.now(ZoneOffset.UTC)
        );

        //## When
        comboDomain.updateModifiedAt();

        //## Then
        assertTrue(comboDomain.getModifiedAt().isAfter(modifiedAt));
    }

    @Test
    void fillDates() {

        //## Given
        final var comboDomain = new ComboDomain(
                null,
                "name",
                20D,
                List.of(new ProductDomain()),
                null,
                null
        );
        assertNull(comboDomain.getId());
        assertNull(comboDomain.getCreateAt());
        assertNull(comboDomain.getModifiedAt());

        //## When
        comboDomain.fillDates();

        //## Then
        assertNotNull(comboDomain.getCreateAt());
        assertNotNull(comboDomain.getModifiedAt());
    }
}