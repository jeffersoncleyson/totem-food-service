package com.totem.food.application.ports.in.dtos.payment;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentFilterDto {

    private String orderId;
    private String token;
    private String status;
}
