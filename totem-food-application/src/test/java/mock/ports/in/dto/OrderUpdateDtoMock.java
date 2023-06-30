package mock.ports.in.dto;

import com.totem.food.application.ports.in.dtos.order.totem.ItemQuantityDto;
import com.totem.food.application.ports.in.dtos.order.totem.OrderUpdateDto;

import java.util.List;

public class OrderUpdateDtoMock {

    public static OrderUpdateDto getMock() {
        var oderUpdate = new OrderUpdateDto();
        oderUpdate.setCombos(List.of(
                new ItemQuantityDto(1, "7518d878"))
        );
        oderUpdate.setProducts(List.of(
                new ItemQuantityDto(1, "7518d878-6cd4-4d74-bf76-c21345f2f7da")
        ));
        return oderUpdate;
    }
}
