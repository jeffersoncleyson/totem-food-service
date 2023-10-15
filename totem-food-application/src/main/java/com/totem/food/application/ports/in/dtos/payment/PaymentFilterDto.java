package com.totem.food.application.ports.in.dtos.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentFilterDto {

    private String orderId;
    private String token;
    private String status;
    private ZonedDateTime timeLastOrders;
}
