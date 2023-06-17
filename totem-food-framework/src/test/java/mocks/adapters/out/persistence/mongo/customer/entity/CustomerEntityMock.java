package mocks.adapters.out.persistence.mongo.customer.entity;

import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class CustomerEntityMock {

    public static CustomerEntity getMock() {
        var entity = new CustomerEntity();
        entity.setId("123");
        entity.setName("Name");
        entity.setCpf("12312312399");
        entity.setEmail("name@name.com");
        entity.setMobile("+5511900112233");
        entity.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
        entity.setCreateAt(ZonedDateTime.now(ZoneOffset.UTC));
        return entity;
    }
}
