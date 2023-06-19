package mocks.entity;

import com.totem.food.framework.adapters.out.persistence.mongo.combo.entity.ComboEntity;

import java.math.BigDecimal;
import java.util.List;

public class ComboEntityMock {

    public static ComboEntity getMock() {
        var comboEntity = new ComboEntity();
        comboEntity.setId("1");
        comboEntity.setName("Combo da casa");
        comboEntity.setPrice(BigDecimal.ZERO);
        comboEntity.setProducts(List.of("1", "2"));
        return comboEntity;
    }
}
