package com.totem.food.application.ports.in.dtos.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentElementDto {

    private String externalReference;
    private String orderStatus;
    private BigDecimal totalPayment;
    private String updatePayment;
    private String externalPaymentId;
}
