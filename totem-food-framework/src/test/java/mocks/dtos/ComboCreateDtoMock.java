package mocks.dtos;

import com.totem.food.application.ports.in.dtos.combo.ComboCreateDto;

import java.math.BigDecimal;
import java.util.List;

public class ComboCreateDtoMock {

    public static ComboCreateDto getMock() {
        var comboCreateDto = new ComboCreateDto();
        comboCreateDto.setName("Combo da casa");
        comboCreateDto.setPrice(BigDecimal.TEN);
        comboCreateDto.setProducts(List.of("1", "2"));
        return comboCreateDto;
    }
}
