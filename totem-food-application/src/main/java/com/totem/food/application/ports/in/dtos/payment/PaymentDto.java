package com.totem.food.application.ports.in.dtos.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    private String id;
    private double price;
    private String status;
    private String qrcodeBase64;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;
}
