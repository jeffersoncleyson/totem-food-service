package com.totem.food.framework.adapters.in.rest.customer.totem;

import com.totem.food.application.ports.in.dtos.customer.CustomerConfirmDto;
import com.totem.food.application.ports.in.dtos.customer.CustomerCreateDto;
import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.in.rest.IConfirmRestApiPort;
import com.totem.food.application.ports.in.rest.ICreateRestApiPort;
import com.totem.food.application.ports.in.rest.IRemoveRestApiPort;
import com.totem.food.application.usecases.commons.IConfirmUseCase;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.application.usecases.commons.IDeleteUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.totem.food.framework.adapters.in.rest.constants.Routes.API_VERSION_1;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.CONFIRM_CUSTOMER;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.CUSTOMER_ID;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.TOTEM_CUSTOMER;

@RestController
@RequestMapping(value = API_VERSION_1 + TOTEM_CUSTOMER)
@AllArgsConstructor
public class TotemCustomerRestApiAdapter implements ICreateRestApiPort<CustomerCreateDto, ResponseEntity<CustomerDto>>,
        IRemoveRestApiPort<String, ResponseEntity<Void>>, IConfirmRestApiPort<CustomerConfirmDto, ResponseEntity<Void>> {

    private final ICreateUseCase<CustomerCreateDto, CustomerDto> createCustomerUseCase;
    private final IDeleteUseCase<String, CustomerDto> iDeleteUseCase;
    private final IConfirmUseCase<Boolean, CustomerConfirmDto> iConfirmUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<CustomerDto> create(@RequestBody CustomerCreateDto item) {
        final var createdItem = createCustomerUseCase.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Void> deleteById(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        iDeleteUseCase.removeItem(authorization);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping(CUSTOMER_ID + CONFIRM_CUSTOMER)
    public ResponseEntity<Void> confirm(CustomerConfirmDto confirm) {
        iConfirmUseCase.confirm(confirm);
        return ResponseEntity.noContent().build();
    }
}
