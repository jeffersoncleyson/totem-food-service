package mocks.adapters.out.persistence.mongo.customer.entity;

import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class CustomerEntityMock {

    public static CustomerEntity getMocks() {
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

    public static CustomerModel getMock() {
        var model = new CustomerModel();
        model.setId("123");
        model.setName("Name");
        model.setCpf("12312312399");
        model.setEmail("name@name.com");
        model.setMobile("+5511900112233");
        model.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
        model.setCreateAt(ZonedDateTime.now(ZoneOffset.UTC));
        return model;
    }
}
