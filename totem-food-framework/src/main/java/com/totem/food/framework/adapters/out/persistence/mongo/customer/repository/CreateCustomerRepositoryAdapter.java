package com.totem.food.framework.adapters.out.persistence.mongo.customer.repository;

import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.mapper.ICustomerEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Component
public class CreateCustomerRepositoryAdapter implements ICreateRepositoryPort<CustomerDomain> {

    @Repository
    protected interface CustomerRepositoryMongoDB extends BaseRepository<CustomerEntity, String> {

    }

    private final CustomerRepositoryMongoDB repository;
    private final ICustomerEntityMapper iCustomerEntityMapper;

    @Override
    public CustomerDomain saveItem(CustomerDomain item) {
        final var entity = iCustomerEntityMapper.toEntity(item);
        final var savedEntity = repository.save(entity);
        return iCustomerEntityMapper.toDomain(savedEntity);
    }
}
