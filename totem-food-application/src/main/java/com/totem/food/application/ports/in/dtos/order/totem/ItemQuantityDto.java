package com.totem.food.application.ports.in.dtos.order.totem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
public class ItemQuantityDto {

    private int qtd;
    private String id;
}
