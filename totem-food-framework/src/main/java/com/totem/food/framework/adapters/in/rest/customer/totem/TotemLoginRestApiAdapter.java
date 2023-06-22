package com.totem.food.framework.adapters.in.rest.customer.totem;

import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/totem/login")
@AllArgsConstructor
public class TotemLoginRestApiAdapter implements ISearchUniqueRepositoryPort<ResponseEntity<CustomerDto>> {

    private final ISearchUseCase<Void, CustomerDto> iSearchUseCase;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<CustomerDto> findById(@RequestHeader String cpf, @RequestHeader String password) {


        final var customer = iSearchUseCase.items(login);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }
}
