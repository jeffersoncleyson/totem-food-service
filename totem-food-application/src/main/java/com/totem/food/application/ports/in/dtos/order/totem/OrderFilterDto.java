package com.totem.food.application.ports.in.dtos.order.totem;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class OrderFilterDto {

    private String cpf;
    private String orderId;
    private Set<String> status;
    private Boolean onlyTreadmill;

}
