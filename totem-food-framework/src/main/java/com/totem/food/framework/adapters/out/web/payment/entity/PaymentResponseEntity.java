package com.totem.food.framework.adapters.out.web.payment.entity;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseEntity {

    private String qrcodeBase64;
    private String qrcode;
}
