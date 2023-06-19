package com.totem.food.application.ports.in.dtos.category;

import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private String id;
    private String name;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;
}
