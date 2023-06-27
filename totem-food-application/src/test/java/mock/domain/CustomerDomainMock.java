package mock.domain;

import com.totem.food.domain.customer.CustomerDomain;

import java.time.ZonedDateTime;

public class CustomerDomainMock {

    public static CustomerDomain getMock() {
        var customerDomain = new CustomerDomain();
        customerDomain.setId("1");
        customerDomain.setName("name");
        customerDomain.setMobile("5511911223344");
        customerDomain.setEmail("customer@gmail.com");
        customerDomain.setCpf("12312312399");
        customerDomain.setPassword("123qwe");
        customerDomain.setModifiedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"));
        customerDomain.setCreateAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"));
        return customerDomain;
    }
}
