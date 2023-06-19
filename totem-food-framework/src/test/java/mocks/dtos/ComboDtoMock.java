package mocks.dtos;

import com.totem.food.application.ports.in.dtos.combo.ComboDto;
import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.framework.adapters.out.persistence.mongo.product.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.List;

public class ComboDtoMock {

    public static ComboDto getMock() {
        var comboDto = new ComboDto();
        comboDto.setName("Combo da casa");
        comboDto.setPrice(BigDecimal.TEN);
        final var productDto = ProductDto.builder().id("1").build();
        comboDto.setProducts(List.of(productDto));
        return comboDto;
    }
}
