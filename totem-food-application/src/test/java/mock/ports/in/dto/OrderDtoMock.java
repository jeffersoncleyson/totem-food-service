package mock.ports.in.dto;

import com.totem.food.application.ports.in.dtos.order.totem.OrderDto;
import com.totem.food.application.ports.in.dtos.product.ProductDto;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class OrderDtoMock {

    public static OrderDto getMock() {
        var orderDto = new OrderDto();
        orderDto.setId("1");
        orderDto.setCustomerId("1");
        orderDto.setProducts(List.of(new ProductDto()));
        orderDto.setStatus("NEW");
        orderDto.setCreateAt(ZonedDateTime.now(ZoneOffset.UTC));
        orderDto.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
        return orderDto;
    }

}
