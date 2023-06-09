package com.totem.food.framework.adapters.out.persistence.mongo.customer.repository;

import com.totem.food.application.ports.in.dtos.customer.CustomerFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.mapper.ICustomerEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class SearchCustomerRepositoryAdapter implements ISearchRepositoryPort<CustomerFilterDto, List<CustomerModel>> {

    @Repository
    protected interface CustomerRepositoryMongoDB extends BaseRepository<CustomerEntity, String> {
    }

    private final CustomerRepositoryMongoDB repository;
    private final ICustomerEntityMapper iCustomerEntityMapper;

    @Override
    public List<CustomerModel> findAll(CustomerFilterDto filterCategoryDto) {
        final var customerModels = new ArrayList<CustomerModel>();
        repository.findAll().forEach(entity -> customerModels.add(iCustomerEntityMapper.toModel(entity)));
        return customerModels;
    }
}
