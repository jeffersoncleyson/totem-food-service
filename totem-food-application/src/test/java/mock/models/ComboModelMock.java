package mock.models;

import com.totem.food.application.ports.out.persistence.combo.ComboModel;
import com.totem.food.domain.combo.ComboDomain;
import mock.domain.ProductDomainMock;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public class ComboModelMock {

    public static ComboModel getMock() {
        var comboModel = new ComboModel();
        comboModel.setId(UUID.randomUUID().toString());
        comboModel.setName("Combo da casa");
        comboModel.setPrice(49.99);
        comboModel.setProducts(List.of(ProductModelMock.getMock()));
        comboModel.setModifiedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"));
        comboModel.setCreateAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"));
        return comboModel;
    }
}
