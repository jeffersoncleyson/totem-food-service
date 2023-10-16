package com.totem.food.framework.adapters.out.web.payment.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class PaymentResponseEntity {

    @JsonProperty("qr_data")
    private String qrcodeBase64;

    @JsonProperty("in_store_order_id")
    private String storeOrderId;

}
