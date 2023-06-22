package com.totem.food.application.usecases.customer;

import com.totem.food.application.exceptions.ElementExistsException;
import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.in.mappers.customer.ICustomerMapper;
import com.totem.food.application.ports.out.persistence.commons.ILoginRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.ILoginUseCase;
import com.totem.food.domain.customer.CustomerDomain;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
@UseCase
public class LoginCustomerUserCase implements ILoginUseCase<CustomerDto> {

    private final ICustomerMapper iCustomerMapper;
    private final ILoginRepositoryPort<Optional<CustomerDomain>> iSearchUniqueRepositoryPort;

    @Override
    public CustomerDto login(String id, String password) {
        var customerDomain = iSearchUniqueRepositoryPort.findByCadastre(id, password)
                .orElseThrow(() -> new ElementExistsException("Incorrect user or password"));
        return iCustomerMapper.toDto(customerDomain);
    }
}
