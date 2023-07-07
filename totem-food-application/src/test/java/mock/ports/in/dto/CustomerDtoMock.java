package mock.ports.in.dto;

import com.totem.food.application.ports.in.dtos.customer.CustomerDto;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class CustomerDtoMock {

    public static CustomerDto getMock() {
        var customerDto = new CustomerDto();
        customerDto.setId("123");
        customerDto.setName("John");
        customerDto.setCpf("12312312399");
        customerDto.setMobile("5511912121212");
        customerDto.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));
        customerDto.setCreateAt(ZonedDateTime.now(ZoneOffset.UTC));
        return customerDto;
    }
}
