package mocks.dtos;

import com.totem.food.application.ports.in.dtos.combo.ComboDto;

import java.math.BigDecimal;
import java.util.List;

public class ComboDtoMock {

    public static ComboDto getMock() {
        var comboDto = new ComboDto();
        comboDto.setName("Combo da casa");
        comboDto.setPrice(BigDecimal.TEN);
        comboDto.setProducts(List.of("1", "2"));
        return comboDto;
    }
}
