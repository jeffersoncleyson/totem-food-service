package com.totem.food.application.ports.out.persistence.product;

import com.totem.food.domain.category.CategoryDomain;
import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {

    private String id;
    private String name;
    private String description;
    private String image;
    private double price;
    private CategoryDomain category;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;
}
