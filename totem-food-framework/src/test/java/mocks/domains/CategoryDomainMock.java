package mocks.domains;

import com.totem.food.domain.category.CategoryDomain;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class CategoryDomainMock {

    public static CategoryDomain getMock() {
        return CategoryDomain.builder()
                .id("123")
                .name("Refrigerante")
                .modifiedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .createAt(ZonedDateTime.now(ZoneOffset.UTC))
                .build();
    }
}
