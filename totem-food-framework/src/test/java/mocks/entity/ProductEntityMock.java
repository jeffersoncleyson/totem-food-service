package mocks.entity;

import com.totem.food.framework.adapters.out.persistence.mongo.category.entity.CategoryEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.product.entity.ProductEntity;

import java.time.ZonedDateTime;

public class ProductEntityMock {

    public static ProductEntity getMock() {
        return ProductEntity.builder()
                .id("1")
                .name("Coca")
                .description("Sabor Cola")
                .image("URL")
                .price(4.99)
                .category(new CategoryEntity())
                .createAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .modifiedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"))
                .build();
    }
}
