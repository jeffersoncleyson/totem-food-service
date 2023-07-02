package com.totem.food.domain.order.enums;

import com.totem.food.domain.exceptions.InvalidEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderStatusEnumDomainTest {

    @Test
    void from() {

        //## Given - When
        var result = OrderStatusEnumDomain.from("IN_PREPARATION");

        //## Then
        assertEquals(OrderStatusEnumDomain.IN_PREPARATION, result);

    }

    @Test
    void fromWhenInvalidEnum() {

        //## Given - When
        var result = assertThrows(InvalidEnum.class, () -> OrderStatusEnumDomain.from(""));

        //## Then
        assertEquals("Invalid value [], allowed status [[RECEIVED, READY, NEW, WAITING_PAYMENT, CANCELED, FINALIZED, IN_PREPARATION]]",
                result.getMessage());

    }

}