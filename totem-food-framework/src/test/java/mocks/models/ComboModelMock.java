package mocks.models;

import com.totem.food.application.ports.out.persistence.combo.ComboModel;
import com.totem.food.application.ports.out.persistence.product.ProductModel;
import com.totem.food.domain.combo.ComboDomain;
import com.totem.food.domain.product.ProductDomain;

import java.util.List;

public class ComboModelMock {

    public static ComboModel getMock() {
        var comboModel = new ComboModel();
        comboModel.setId("1");
        comboModel.setName("Combo da casa");
        comboModel.setPrice(Double.MIN_NORMAL);
        final var productModel = ProductModel.builder().id("1").build();
        comboModel.setProducts(List.of(productModel));
        return comboModel;
    }
}
