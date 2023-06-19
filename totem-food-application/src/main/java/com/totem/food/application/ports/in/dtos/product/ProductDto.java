package com.totem.food.application.ports.in.dtos.product;

import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String id;
    private String name;
    private String description;
    private String image;
    private double price;
    private CategoryDto category;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;
}
