package com.totem.food.application.ports.in.dtos.order.totem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPathParamsDto {

    private String orderId;
    private String statusName;
}
