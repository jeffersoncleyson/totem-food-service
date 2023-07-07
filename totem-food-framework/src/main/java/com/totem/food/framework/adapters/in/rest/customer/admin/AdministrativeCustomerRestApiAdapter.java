package com.totem.food.framework.adapters.in.rest.customer.admin;

import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.in.dtos.customer.CustomerFilterDto;
import com.totem.food.application.ports.in.rest.ISearchRestApiPort;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.totem.food.framework.adapters.in.rest.constants.Routes.ADM_CUSTOMER;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.API_VERSION_1;

@RestController
@RequestMapping(value = API_VERSION_1 + ADM_CUSTOMER)
@AllArgsConstructor
public class AdministrativeCustomerRestApiAdapter implements ISearchRestApiPort<CustomerFilterDto, ResponseEntity<List<CustomerDto>>> {

    private final ISearchUseCase<CustomerFilterDto, List<CustomerDto>> iSearchCustomerUseCase;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<List<CustomerDto>> listAll(@RequestBody(required = false) CustomerFilterDto customerFilterDto) {
        final var customersDto = iSearchCustomerUseCase.items(customerFilterDto);
        return ResponseEntity.status(HttpStatus.OK).body(customersDto);
    }
}
