package com.totem.food.application.ports.in.dtos.product;

import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
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
