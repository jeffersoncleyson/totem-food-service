package com.totem.food.framework.adapters.in.rest.customer.admin;

import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.in.dtos.customer.CustomerFilterDto;
import com.totem.food.application.ports.in.rest.ISearchRestApiPort;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.framework.adapters.in.rest.constants.Routes;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = Routes.VERSION_1 + Routes.ADM_CUSTOMER)
@AllArgsConstructor
public class AdministrativeCustomerRestApiAdapter implements ISearchRestApiPort<CustomerFilterDto, ResponseEntity<List<CustomerDto>>> {

    private final ISearchUseCase<CustomerFilterDto, List<CustomerDto>> iSearchCustomerUseCase;

    @GetMapping
    @Override
    public ResponseEntity<List<CustomerDto>> listAll(@RequestBody(required = false) CustomerFilterDto customerFilterDto) {
        return new ResponseEntity<>(iSearchCustomerUseCase.items(customerFilterDto), HttpStatus.OK);
    }
}
