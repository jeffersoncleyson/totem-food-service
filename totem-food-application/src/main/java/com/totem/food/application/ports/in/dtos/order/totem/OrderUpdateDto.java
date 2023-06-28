package com.totem.food.application.ports.in.dtos.order.totem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateDto {

    private List<ItemQuantityDto> products;
    private List<ItemQuantityDto> combos;

    public boolean isOrderValid(){
        return CollectionUtils.isNotEmpty(products) || CollectionUtils.isNotEmpty(combos);
    }
}
