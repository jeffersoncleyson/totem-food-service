package com.totem.food.application.ports.in.dtos.combo;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComboFilterDto {

    private String name;
    private List<String> ids;
}
