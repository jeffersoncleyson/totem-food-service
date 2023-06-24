package com.totem.food.framework.adapters.out.web.payment.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestEntity {

    private String orderId;
    private String customerId;
    private double price;
    private String token;
}
