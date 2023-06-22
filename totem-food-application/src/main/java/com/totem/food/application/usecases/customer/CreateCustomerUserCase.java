package com.totem.food.application.usecases.customer;

import com.totem.food.application.exceptions.ElementExistsException;
import com.totem.food.application.ports.in.dtos.customer.CustomerCreateDto;
import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.in.mappers.customer.ICustomerMapper;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IExistsRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.domain.customer.CustomerDomain;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@UseCase
public class CreateCustomerUserCase implements ICreateUseCase<CustomerCreateDto, CustomerDto> {

    private final ICustomerMapper iCustomerMapper;
    private final ICreateRepositoryPort<CustomerDomain> iCreateRepositoryPort;
    private final IExistsRepositoryPort<CustomerDomain, Boolean> iExistsRepositoryPort;

    @Override
    public CustomerDto createItem(CustomerCreateDto item) {
        final var customerDomain = iCustomerMapper.toDomain(item);
        customerDomain.validEmailAddress();
        customerDomain.validateName();
        customerDomain.fillDates();

        if (Boolean.TRUE.equals(iExistsRepositoryPort.exists(customerDomain))) {
            throw new ElementExistsException(String.format("Customer [%s] already registered", item.getName()));
        }

        final var customerDomainSaved = iCreateRepositoryPort.saveItem(customerDomain);
        return iCustomerMapper.toDto(customerDomainSaved);
    }
}
