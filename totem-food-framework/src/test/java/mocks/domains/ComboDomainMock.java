package mocks.domains;

import com.totem.food.domain.combo.ComboDomain;
import com.totem.food.domain.product.ProductDomain;

import java.math.BigDecimal;
import java.util.List;

public class ComboDomainMock {

    public static ComboDomain getMock() {
        var comboDomain = new ComboDomain();
        comboDomain.setId("1");
        comboDomain.setName("Combo da casa");
        comboDomain.setPrice(Double.MIN_NORMAL);
        final var productDomain = ProductDomain.builder().id("1").build();
        comboDomain.setProducts(List.of(productDomain));
        return comboDomain;
    }
}
