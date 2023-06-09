package com.totem.food.application.ports.in.dtos.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterCategoryDto {
    private String categoryName;
}
