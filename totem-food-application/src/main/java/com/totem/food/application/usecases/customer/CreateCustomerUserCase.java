package com.totem.food.application.usecases.customer;

import com.totem.food.application.exceptions.ElementExistsException;
import com.totem.food.application.ports.in.dtos.customer.CustomerCreateDto;
import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.in.mappers.customer.ICustomerMapper;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IExistsRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@UseCase
public class CreateCustomerUserCase implements ICreateUseCase<CustomerCreateDto, CustomerDto> {

    private final ICustomerMapper iCustomerMapper;
    private final ICreateRepositoryPort<CustomerModel> iCreateRepositoryPort;
    private final IExistsRepositoryPort<CustomerModel, Boolean> iExistsRepositoryPort;

    @Override
    public CustomerDto createItem(CustomerCreateDto item) {
        final var customerDomain = iCustomerMapper.toDomain(item);
        customerDomain.validEmailAddress();
        customerDomain.validateName();
        customerDomain.fillDates();

        final var model = iCustomerMapper.toModel(customerDomain);

        if (Boolean.TRUE.equals(iExistsRepositoryPort.exists(model))) {
            throw new ElementExistsException(String.format("Customer with cpf [%s] already registered", item.getCpf()));
        }

        final var customerModel = iCustomerMapper.toModel(customerDomain);
        final var customerDomainSaved = iCreateRepositoryPort.saveItem(customerModel);
        return iCustomerMapper.toDto(customerDomainSaved);
    }
}
