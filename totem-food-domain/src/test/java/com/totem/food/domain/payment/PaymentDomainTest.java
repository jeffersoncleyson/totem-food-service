package com.totem.food.domain.payment;

import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.domain.order.totem.OrderDomain;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentDomainTest {


    @Test
    void updateStatusWhenInputStatusIsNotCompleted() {

        //## Given
        var payment = PaymentDomain.builder()
                .id("123")
                .order(new
                        OrderDomain())
                .customer(new CustomerDomain())
                .price(100.0)
                .token("abc123")
                .status(PaymentDomain.PaymentStatus.PENDING)
                .modifiedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .createAt(ZonedDateTime.now(ZoneOffset.UTC))
                .build();

        //## When
        payment.updateStatus(PaymentDomain.PaymentStatus.PENDING);

        //## Then
        assertNotNull(payment.getModifiedAt());
        assertEquals(PaymentDomain.PaymentStatus.PENDING, payment.getStatus());
    }

    @Test
    void updateStatusWhenInputStatusIsCompleted() {

        //## Given
        var payment = PaymentDomain.builder()
                .id("123")
                .order(new
                        OrderDomain())
                .customer(new CustomerDomain())
                .price(100.0)
                .token("abc123")
                .status(PaymentDomain.PaymentStatus.PENDING)
                .modifiedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .createAt(ZonedDateTime.now(ZoneOffset.UTC))
                .build();

        //## When
        payment.updateStatus(PaymentDomain.PaymentStatus.COMPLETED);

        //## Then
        assertNotNull(payment.getModifiedAt());
        assertSame(PaymentDomain.PaymentStatus.COMPLETED, payment.getStatus());
    }

    @Test
    void updateModifiedAt() {

        //## Given
        var payment = PaymentDomain.builder()
                .id("123")
                .order(new
                        OrderDomain())
                .customer(new CustomerDomain())
                .price(100.0)
                .token("abc123")
                .status(PaymentDomain.PaymentStatus.PENDING)
                .modifiedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .createAt(ZonedDateTime.now(ZoneOffset.UTC))
                .build();

        //## When
        payment.updateModifiedAt();
        var actualModifiedAt = ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(1);

        //## Then
        assertTrue(actualModifiedAt.isAfter(payment.getModifiedAt()));
    }

    @Test
    void fillDatesWhenIdIsEmpty() {

        //## Given
        var payment = new PaymentDomain();

        //## When
        payment.fillDates();

        //## Then
        assertNotNull(payment.getCreateAt());
        assertNotNull(payment.getModifiedAt());
    }

}