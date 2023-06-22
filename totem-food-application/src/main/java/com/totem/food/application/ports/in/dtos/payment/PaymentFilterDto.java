package com.totem.food.application.ports.in.dtos.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentFilterDto {

    private String orderId;
    private String customerId;
    private String token;
}
