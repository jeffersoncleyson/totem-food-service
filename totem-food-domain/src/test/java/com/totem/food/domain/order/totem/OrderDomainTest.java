package com.totem.food.domain.order.totem;

import com.totem.food.domain.exceptions.InvalidStatusTransition;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import com.totem.food.domain.product.ProductDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("OrderDomain")
class OrderDomainTest {

    @Test
    void clearProducts() {

        //## Given
        var order = OrderDomain.builder()
                .products(List.of(new ProductDomain()))
                .build();

        assertNotNull(order.getProducts());

        //## When
        order.clearProducts();

        //## Then
        assertNull(order.getProducts());
    }

    @Test
    void updateModifiedAt() {

        //## Given
        var order = OrderDomain.builder().build();

        //## When
        order.updateModifiedAt();

        var currentDateTime = ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(1);

        //## Then
        assertTrue(currentDateTime.isAfter(order.getModifiedAt()));
    }

    @Test
    void fillDatesWhenIdIsNotEmpty() {

        //## Given
        var orderDomain = new OrderDomain();
        orderDomain.setId(UUID.randomUUID().toString());

        //## When
        orderDomain.fillDates();

        //## Then
        assertNull(orderDomain.getCreateAt());
        assertNull(orderDomain.getModifiedAt());
    }

    @Test
    void fillDatesWhenIdIsEmpty() {

        //## Given
        var orderDomain = new OrderDomain();
        orderDomain.fillDates();

        //## When
        var now = ZonedDateTime.now(ZoneOffset.UTC);

        //## Then
        assertEquals(now.getZone(), orderDomain.getCreateAt().getZone());
        assertEquals(now.getZone(), orderDomain.getModifiedAt().getZone());
        assertTrue(orderDomain.getCreateAt().isEqual(now) || orderDomain.getCreateAt().isBefore(now));
        assertTrue(orderDomain.getModifiedAt().isEqual(now) || orderDomain.getModifiedAt().isBefore(now));
    }

    @Test
    void calculatePriceWithNoProductsOrCombos() {

        //## Given
        var order = OrderDomain.builder().build();

        //## When
        order.calculatePrice();

        //## Then
        assertEquals(0.0, order.getPrice());
    }


    @Test
    void calculatePriceWithProducts() {

        //## Given
        var product1 = new ProductDomain();
        product1.setId(UUID.randomUUID().toString());
        product1.setName("X-Tudo");
        product1.setPrice(9.99);

        var product2 = new ProductDomain();
        product2.setId(UUID.randomUUID().toString());
        product2.setName("Coca-Cola");
        product2.setPrice(2.99);

        List<ProductDomain> products = Arrays.asList(product1, product2);

        var order = OrderDomain.builder()
                .products(products)
                .build();

        //## When
        order.calculatePrice();

        //## Then
        assertEquals(12.98, order.getPrice());
    }

    @Test
    void updateOrderStatusWhenTransitionIsNotAllowedThenThrowException() {

        //## Given
        var order = OrderDomain.builder().status(OrderStatusEnumDomain.NEW).build();
        var newStatus = OrderStatusEnumDomain.IN_PREPARATION;

        //## When
        var exception = assertThrows(InvalidStatusTransition.class, () -> order.updateOrderStatus(newStatus));

        //## Then
        assertEquals("Transition from [NEW] to [IN_PREPARATION] failed, allowed status [[RECEIVED, READY, NEW, WAITING_PAYMENT, CANCELED, FINALIZED, IN_PREPARATION]]",
                exception.getMessage());

    }

    @Test
    void updateOrderStatusWhenWaitingPaymentStatusIsReceived() {

        //## Given
        var order = OrderDomain.builder()
                .status(OrderStatusEnumDomain.WAITING_PAYMENT)
                .build();

        //## When
        order.updateOrderStatus(OrderStatusEnumDomain.RECEIVED);

        //## Then
        assertEquals(OrderStatusEnumDomain.RECEIVED, order.getStatus());
        assertNotNull(order.getReceivedAt());
    }

    @Test
    void updateOrderStatusWhenTransitionIsAllowed() {

        //## Given
        var order = OrderDomain.builder()
                .id("123")
                .status(OrderStatusEnumDomain.NEW)
                .build();
        var newStatus = OrderStatusEnumDomain.WAITING_PAYMENT;

        //## When
        order.updateOrderStatus(newStatus);

        //## Then
        assertEquals(newStatus, order.getStatus());
    }
}