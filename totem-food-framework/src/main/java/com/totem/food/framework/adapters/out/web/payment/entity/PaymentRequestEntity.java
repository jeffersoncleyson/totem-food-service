package com.totem.food.framework.adapters.out.web.payment.entity;


import lombok.*;

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
