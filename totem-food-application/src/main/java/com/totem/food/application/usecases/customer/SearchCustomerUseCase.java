package com.totem.food.application.usecases.customer;

import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.in.dtos.customer.CustomerFilterDto;
import com.totem.food.application.ports.in.mappers.customer.ICustomerMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.domain.customer.CustomerDomain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SearchCustomerUseCase implements ISearchUseCase<CustomerFilterDto, List<CustomerDto>> {

    private final ICustomerMapper iCustomerMapper;
    private final ISearchRepositoryPort<CustomerFilterDto, CustomerDomain> iCustomerRepositoryPort;

    @Override
    public List<CustomerDto> items(CustomerFilterDto customerFilterDto) {
        return iCustomerRepositoryPort.findAll(customerFilterDto)
                .stream()
                .map(iCustomerMapper::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
