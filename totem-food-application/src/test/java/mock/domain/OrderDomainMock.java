package mock.domain;

import com.totem.food.domain.combo.ComboDomain;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.domain.product.ProductDomain;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderDomainMock {

    public static OrderDomain getStatusNewMock() {
        return getOrderDomain(OrderStatusEnumDomain.NEW);
    }

    public static OrderDomain getOrderDomain(OrderStatusEnumDomain orderStatusEnumDomain) {
        return OrderDomain.builder()
                .id("1")
                .customer(CustomerDomainMock.getMock())
                .products(List.of(new ProductDomain()))
                .combos(List.of(new ComboDomain()))
                .price(49.99)
                .status(orderStatusEnumDomain)
                .modifiedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .createAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .receivedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .build();
    }

    public static OrderDomain getStatusWaitingPaymentMock() {
        return OrderDomain.builder()
                .id("1")
                .customer(CustomerDomainMock.getMock())
                .products(List.of(new ProductDomain()))
                .combos(List.of(new ComboDomain()))
                .price(49.99)
                .status(OrderStatusEnumDomain.WAITING_PAYMENT)
                .modifiedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .createAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .receivedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .build();
    }
}
