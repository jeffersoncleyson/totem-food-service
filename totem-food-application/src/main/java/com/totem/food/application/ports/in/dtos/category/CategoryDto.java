package com.totem.food.application.ports.in.dtos.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private String id;
    private String name;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;
}
