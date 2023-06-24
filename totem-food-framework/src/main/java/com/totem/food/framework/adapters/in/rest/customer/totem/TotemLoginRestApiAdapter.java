package com.totem.food.framework.adapters.in.rest.customer.totem;

import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.usecases.commons.ILoginUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.totem.food.framework.adapters.in.rest.constants.Routes.API_VERSION_1;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.TOTEM_LOGIN;

@RestController
@RequestMapping(value = API_VERSION_1 + TOTEM_LOGIN)
@AllArgsConstructor
public class TotemLoginRestApiAdapter {

    private final ILoginUseCase<CustomerDto> iLoginUseCase;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerDto> findById(@RequestHeader String cpf, @RequestHeader String password) {
        final var customer = iLoginUseCase.login(cpf, password);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }
}
