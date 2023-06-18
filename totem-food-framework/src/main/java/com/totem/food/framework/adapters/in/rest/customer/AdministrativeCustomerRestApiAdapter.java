package com.totem.food.framework.adapters.in.rest.customer;

import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.in.dtos.customer.CustomerFilterDto;
import com.totem.food.application.ports.in.rest.ISearchRestApiPort;
import com.totem.food.application.ports.in.rest.ISearchUniqueRestApiPort;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/administrative/customer")
@AllArgsConstructor
public class AdministrativeCustomerRestApiAdapter implements ISearchRestApiPort<CustomerFilterDto, ResponseEntity<List<CustomerDto>>>,
        ISearchUniqueRestApiPort<String, ResponseEntity<CustomerDto>> {

    private final ISearchUseCase<CustomerFilterDto, List<CustomerDto>> iSearchCustomerUseCase;

    @Override
    public ResponseEntity<List<CustomerDto>> listAll(@RequestBody(required = false) CustomerFilterDto customerFilterDto) {
        return new ResponseEntity<>(iSearchCustomerUseCase.items(customerFilterDto), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CustomerDto> getById(String id) {
        return null;
    }
}
