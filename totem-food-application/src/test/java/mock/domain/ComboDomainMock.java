package mock.domain;

import com.totem.food.domain.combo.ComboDomain;

import java.time.ZonedDateTime;
import java.util.List;

public class ComboDomainMock {

    public static ComboDomain getMock() {
        var comboDomain = new ComboDomain();
        comboDomain.setId("1");
        comboDomain.setName("Combo da casa");
        comboDomain.setPrice(49.99);
        comboDomain.setProducts(List.of(ProductDomainMock.getMock()));
        comboDomain.setModifiedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"));
        comboDomain.setCreateAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"));
        return comboDomain;
    }
}
