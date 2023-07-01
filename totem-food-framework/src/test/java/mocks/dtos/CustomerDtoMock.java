package mocks.dtos;

import com.totem.food.application.ports.in.dtos.customer.CustomerDto;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

public class CustomerDtoMock {

    public static CustomerDto getMock() {
        var customerDto = new CustomerDto();
        customerDto.setId(UUID.randomUUID().toString());
        customerDto.setName("John");
        customerDto.setCpf("12312312399");
        customerDto.setEmail("customer@gmail.com");
        customerDto.setMobile("5511911223344");
        customerDto.setCreateAt(ZonedDateTime.now(ZoneOffset.UTC));
        customerDto.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
        return customerDto;
    }

}
