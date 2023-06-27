package mock;

import com.totem.food.domain.combo.ComboDomain;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.domain.product.ProductDomain;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderDomainMock {

    public static OrderDomain getMock() {
        var orderDomain = new OrderDomain();
        orderDomain.setId("1");
        orderDomain.setCustomer(CustomerDomainMock.getMock());
        orderDomain.setProducts(List.of(new ProductDomain()));
        orderDomain.setCombos(List.of(new ComboDomain()));
        orderDomain.setPrice(49.99);
        orderDomain.setModifiedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"));
        orderDomain.setCreateAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"));
        orderDomain.setOrderReceivedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"));
        return orderDomain;
    }
}
