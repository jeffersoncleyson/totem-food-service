package com.totem.food.framework.adapters.out.persistence.mongo.product.entity;

import com.totem.food.framework.adapters.out.persistence.mongo.category.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "product")
public class ProductEntity {

    @Id
    private String id;
    private String name;
    private String description;
    private String image;
    private double price;

    @DBRef
    private CategoryEntity category;

    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;
}
