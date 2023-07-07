package com.totem.food.framework.adapters.out.persistence.mongo.category.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "category")
public class CategoryEntity {

    @Id
    private String id;
    private String name;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;
}
