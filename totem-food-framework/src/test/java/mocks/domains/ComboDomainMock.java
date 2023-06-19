package mocks.domains;

import com.totem.food.domain.combo.ComboDomain;

import java.math.BigDecimal;
import java.util.List;

public class ComboDomainMock {

    public static ComboDomain getMock() {
        var comboDomain = new ComboDomain();
        comboDomain.setId("1");
        comboDomain.setName("Combo da casa");
        comboDomain.setPrice(BigDecimal.ZERO);
        comboDomain.setProducts(List.of("1", "2"));
        return comboDomain;
    }
}
