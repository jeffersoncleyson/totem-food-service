package mocks.domains;

import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import com.totem.food.domain.order.totem.OrderDomain;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class OrderDomainMock {

    public static OrderDomain getStatusNewMock() {
        return OrderDomain.builder()
                .id("1")
                .customer(CustomerDomainMock.getMock())
                .products(List.of(ProductDomainMock.getMock()))
                .price(25.0)
                .status(OrderStatusEnumDomain.NEW)
                .modifiedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .createAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .receivedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .build();
    }
}
