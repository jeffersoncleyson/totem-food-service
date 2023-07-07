package mocks.entity;

import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

public class CustomerEntityMock {

    public static CustomerEntity getMock() {
        return CustomerEntity.builder()
                .id(UUID.randomUUID().toString())
                .name("John")
                .cpf("12312312399")
                .email("customer@gmail.com")
                .mobile("5511911223344")
                .password("123qwe")
                .createAt(ZonedDateTime.now(ZoneOffset.UTC))
                .modifiedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .build();
    }
}
