package com.totem.food.framework.adapters.out.web.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElementPaymentResponseEntity {

    private String id;
    private String status;

}
