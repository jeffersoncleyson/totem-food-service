package com.totem.food.application.ports.in.dtos.order.totem;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderFilterDto {

    private String customerId;
    private String orderId;

}
