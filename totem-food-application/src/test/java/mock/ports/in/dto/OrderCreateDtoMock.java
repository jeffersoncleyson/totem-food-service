package mock.ports.in.dto;

import com.totem.food.application.ports.in.dtos.order.totem.OrderCreateDto;

import java.util.List;

public class OrderCreateDtoMock {

    public static OrderCreateDto getMock() {
        var orderCreate = new OrderCreateDto();
        orderCreate.setCustomerId("1");
        orderCreate.setProducts(List.of("123", "456"));
        orderCreate.setCombos(List.of("123", "456"));
        return orderCreate;
    }
}
