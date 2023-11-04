package com.totem.food.framework.adapters.in.rest.payment.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(NON_EMPTY)
public class PaymentItemsRequestEntity {

    private String skuNumber;
    private String category;
    private String title;
    private String description;
    private Integer quantity;

    @Builder.Default
    private String unitMeasure = "unit";

    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
}
