package com.totem.food.application.ports.in.dtos.order.totem;

import com.totem.food.application.ports.in.dtos.combo.ComboDto;
import com.totem.food.application.ports.in.dtos.product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private String id;
    private String customerId;
    private List<ProductDto> products;
    private List<ComboDto> combos;
    private String status;
    private double price;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;
}
