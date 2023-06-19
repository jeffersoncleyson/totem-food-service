package com.totem.food.application.ports.in.dtos.product;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterDto {

    private String name;
    private List<String> ids;
}
