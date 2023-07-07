package mock.ports.in.dto;

import com.totem.food.application.ports.in.dtos.order.totem.ItemQuantityDto;
import com.totem.food.application.ports.in.dtos.order.totem.OrderUpdateDto;

import java.util.List;

public class OrderUpdateDtoMock {

    public static OrderUpdateDto getMock(String productId) {
        var oderUpdate = new OrderUpdateDto();
        oderUpdate.setProducts(List.of(new ItemQuantityDto(1, productId)));
        return oderUpdate;
    }
}
