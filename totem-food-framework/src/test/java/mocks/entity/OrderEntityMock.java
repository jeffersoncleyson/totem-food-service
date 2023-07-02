package mocks.entity;

import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.entity.OrderEntity;
import org.bson.types.ObjectId;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.totem.food.domain.order.enums.OrderStatusEnumDomain.NEW;

public class OrderEntityMock {

    public static OrderEntity getMock() {
        return OrderEntity.builder()
                .id(new ObjectId().toHexString())
                .customer(CustomerEntityMock.getMock())
                .products(List.of(ProductEntityMock.getMock()))
                .combos(List.of(ComboEntityMock.getMock()))
                .status(String.valueOf(NEW))
                .price(25.0)
                .modifiedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .createAt(ZonedDateTime.now(ZoneOffset.UTC))
                .receivedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .build();
    }
}
