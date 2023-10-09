package com.totem.food.application.ports.in.dtos.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentQRCodeDto {

    private String qrcodeBase64;
    private String storeOrderId;
    private String status;
    private String paymentId;
}
