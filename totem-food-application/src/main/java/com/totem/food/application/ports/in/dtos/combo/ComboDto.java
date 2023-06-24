package com.totem.food.application.ports.in.dtos.combo;

import com.totem.food.application.ports.in.dtos.product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComboDto {

    private String name;
    private BigDecimal price;
    private List<ProductDto> products;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;

}
