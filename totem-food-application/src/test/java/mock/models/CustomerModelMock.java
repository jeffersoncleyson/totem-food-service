package mock.models;

import com.totem.food.application.ports.out.persistence.customer.CustomerModel;

import java.time.ZonedDateTime;

public class CustomerModelMock {

    public static CustomerModel getMock() {
        var customerModel = new CustomerModel();
        customerModel.setId("1");
        customerModel.setName("name");
        customerModel.setMobile("5511911223344");
        customerModel.setEmail("customer@gmail.com");
        customerModel.setCpf("12312312399");
        customerModel.setPassword("123qwe");
        customerModel.setModifiedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"));
        customerModel.setCreateAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"));
        return customerModel;
    }
}
