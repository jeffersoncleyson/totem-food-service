package com.totem.food.application.ports.in.dtos.combo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComboCreateDto {

    private String name;
    private BigDecimal price;
    private List<String> products;
    
}
