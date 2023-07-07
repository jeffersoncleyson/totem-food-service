package mocks.models;

import com.totem.food.application.ports.out.persistence.order.totem.OrderModel;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import mocks.domains.CustomerDomainMock;
import mocks.domains.ProductDomainMock;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class OrderModelMock {

    public static OrderModel getOrderModel(OrderStatusEnumDomain orderStatusEnumDomain) {
        return OrderModel.builder()
                .id("1")
                .customer(CustomerDomainMock.getMock())
                .products(List.of(ProductDomainMock.getMock()))
                .price(25.0)
                .status(orderStatusEnumDomain)
                .modifiedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .createAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .receivedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .build();
    }
}
