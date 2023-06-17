package com.totem.food.application.ports.in.dtos.product;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDto {

    private String name;
    private String description;
    private String image;
    private double price;
    private String category;
}
