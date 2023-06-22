package com.totem.food.application.ports.in.dtos.order.totem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateDto {

    private List<String> products;
    private List<String> combos;
}
