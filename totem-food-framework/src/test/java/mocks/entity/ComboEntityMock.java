package mocks.entity;

import com.totem.food.domain.product.ProductDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.combo.entity.ComboEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.product.entity.ProductEntity;

import java.math.BigDecimal;
import java.util.List;

public class ComboEntityMock {

    public static ComboEntity getMock() {
        var comboEntity = new ComboEntity();
        comboEntity.setId("1");
        comboEntity.setName("Combo da casa");
        comboEntity.setPrice(BigDecimal.ZERO);
        final var productEntity = ProductEntity.builder().id("1").build();
        comboEntity.setProducts(List.of(productEntity));
        return comboEntity;
    }
}
