package com.totem.food.framework.adapters.out.persistence.mongo.customer.repository;

import com.totem.food.application.ports.out.persistence.customer.ICustomerRepositoryPort;
import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.mapper.ICustomerEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class CustomerRepositoryAdapter implements ICustomerRepositoryPort<CustomerDomain> {

    @Repository
    protected interface CustomerRepositoryMongoDB extends BaseRepository<CustomerEntity, String> {
    }

    private final CustomerRepositoryMongoDB repository;
    private final ICustomerEntityMapper iCustomerEntityMapper;

    @Override
    public List<CustomerDomain> findAll() {
        final var customersDomain = new ArrayList<CustomerDomain>();
        repository.findAll().forEach(entity -> customersDomain.add(iCustomerEntityMapper.toDomain(entity)));
        return customersDomain;
    }

    @Override
    public Optional<CustomerDomain> findById(String id) {
        return Optional.empty();
    }

}
