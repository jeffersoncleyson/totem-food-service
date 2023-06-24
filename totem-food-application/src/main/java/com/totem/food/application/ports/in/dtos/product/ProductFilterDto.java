package com.totem.food.application.ports.in.dtos.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductFilterDto {

    private String name;
    private List<String> ids;
    private Set<String> categoryId;
}
