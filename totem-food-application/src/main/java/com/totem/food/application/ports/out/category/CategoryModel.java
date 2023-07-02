package com.totem.food.application.ports.out.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryModel {

    private String id;
    private String name;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;
}
