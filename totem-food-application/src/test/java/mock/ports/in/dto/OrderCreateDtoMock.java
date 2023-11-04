package mock.ports.in.dto;

import com.totem.food.application.ports.in.dtos.order.totem.ItemQuantityDto;
import com.totem.food.application.ports.in.dtos.order.totem.OrderCreateDto;

import java.util.List;

public class OrderCreateDtoMock {

    public static OrderCreateDto getMock(String productId) {
        var orderCreate = new OrderCreateDto();
        orderCreate.setProducts(List.of(new ItemQuantityDto(1, productId)));
        return orderCreate;
    }
}