package mock.domain;

import com.totem.food.domain.category.CategoryDomain;
import com.totem.food.domain.product.ProductDomain;

import java.time.ZonedDateTime;
import java.util.UUID;

public class ProductDomainMock {

    public static ProductDomain getMock() {
        return ProductDomain.builder()
                .id(UUID.randomUUID().toString())
                .name("Coca-cola")
                .description("Sabor cola")
                .image("URL")
                .price(4.99)
                .category(new CategoryDomain())
                .createAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .modifiedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .build();
    }
}
