package com.totem.food.domain.order.admin;

import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("OrderAdminDomain")
class OrderAdminDomainTest {

    @Test
    void calcWaitTimeWhenOrderIsNeitherInProgressNorFinalized() {

        //## Given
        var order = new OrderAdminDomain();
        order.setReceivedAt(ZonedDateTime.now(ZoneOffset.UTC));
        order.setStatus(OrderStatusEnumDomain.NEW.key);

        //## When
        order.calcWaitTime();

        //## Then
        assertEquals(0, order.getWaitTime());
    }

    @Test
    void calcWaitTimeWhenOrderIsFinalized() {

        //## Given
        var receivedAt = ZonedDateTime.now(ZoneOffset.UTC).minusHours(2);
        var modifiedAt = ZonedDateTime.now(ZoneOffset.UTC);
        var order = OrderAdminDomain.builder()
                .receivedAt(receivedAt)
                .modifiedAt(modifiedAt)
                .status(OrderStatusEnumDomain.FINALIZED.key)
                .build();

        //## When
        order.calcWaitTime();

        //## Then
        assertEquals(Duration.between(receivedAt, modifiedAt).toMinutes(), order.getWaitTime());
    }

    @Test
    void calcWaitTimeWhenOrderIsInProgress() {

        //## Given
        var order = new OrderAdminDomain();
        order.setReceivedAt(ZonedDateTime.now(ZoneOffset.UTC));
        order.setStatus(OrderStatusEnumDomain.RECEIVED.key);
        order.setWaitTime(0);

        //## When
        order.calcWaitTime();

        var duration = Duration.between(order.getReceivedAt(), order.getReceivedAt().plusMinutes(1));

        order.setWaitTime(duration.toMinutes());
        order.setStatus(OrderStatusEnumDomain.IN_PREPARATION.key);

        //## Then
        assertEquals(1, order.getWaitTime());
    }

}