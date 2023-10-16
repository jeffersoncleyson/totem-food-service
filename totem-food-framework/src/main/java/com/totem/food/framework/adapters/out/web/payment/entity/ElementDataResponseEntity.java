package com.totem.food.framework.adapters.out.web.payment.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElementDataResponseEntity {

    @JsonProperty("external_reference")
    private String externalReference;

    @JsonProperty("order_status")
    private String orderStatus;

    @JsonProperty("paid_amount")
    private BigDecimal totalPayment;

    @JsonProperty("last_updated")
    private String updatePayment;

    @JsonProperty("payments")
    private List<ElementPaymentResponseEntity> payments;

}
