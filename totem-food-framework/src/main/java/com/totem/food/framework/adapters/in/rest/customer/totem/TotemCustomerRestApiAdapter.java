package com.totem.food.framework.adapters.in.rest.customer.totem;

import com.totem.food.application.ports.in.dtos.customer.CustomerCreateDto;
import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.in.rest.ICreateRestApiPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/totem/customer")
@AllArgsConstructor
public class TotemCustomerRestApiAdapter implements ICreateRestApiPort<CustomerCreateDto, ResponseEntity<CustomerDto>> {

    private final ICreateUseCase<CustomerCreateDto, CustomerDto> createCustomerUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<CustomerDto> create(@RequestBody CustomerCreateDto item) {
        final var createdItem = createCustomerUseCase.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }
}
