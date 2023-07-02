package mock.models;

import com.totem.food.application.ports.out.persistence.product.ProductModel;
import com.totem.food.domain.category.CategoryDomain;

import java.time.ZonedDateTime;
import java.util.UUID;

public class ProductModelMock {

    public static ProductModel getMock() {
        return ProductModel.builder()
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
