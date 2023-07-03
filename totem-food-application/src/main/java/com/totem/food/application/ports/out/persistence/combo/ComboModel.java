package com.totem.food.application.ports.out.persistence.combo;

import com.totem.food.application.ports.out.persistence.product.ProductModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComboModel {

    private String id;
    private String name;
    private double price;
    private List<ProductModel> products;
    private ZonedDateTime modifiedAt;
    private ZonedDateTime createAt;
}
