package mocks.models;

import com.totem.food.application.ports.out.persistence.category.CategoryModel;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class CategoryModelMock {

    public static CategoryModel getMock() {
        return CategoryModel.builder()
                .id("123")
                .name("Refrigerante")
                .modifiedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .createAt(ZonedDateTime.now(ZoneOffset.UTC))
                .build();
    }
}
