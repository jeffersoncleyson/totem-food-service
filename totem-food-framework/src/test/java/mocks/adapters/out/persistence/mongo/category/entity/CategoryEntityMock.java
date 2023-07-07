package mocks.adapters.out.persistence.mongo.category.entity;

import com.totem.food.framework.adapters.out.persistence.mongo.category.entity.CategoryEntity;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class CategoryEntityMock {

    public static CategoryEntity getMock() {
        var entity = new CategoryEntity();
        entity.setId("123");
        entity.setName("Refrigerante");
        entity.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
        entity.setCreateAt(ZonedDateTime.now(ZoneOffset.UTC));
        return entity;
    }
}
