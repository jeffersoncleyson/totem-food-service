package com.totem.food.framework.adapters.out.persistence.mongo.customer.repository;

import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.mapper.ICustomerEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@AllArgsConstructor
@Component
public class SearchUniqueCustomerRepositoryAdapter implements ISearchUniqueRepositoryPort<Optional<CustomerDomain>> {

    @Repository
    protected interface CustomerRepositoryMongoDB extends BaseRepository<CustomerEntity, String> {
    }

    private final CustomerRepositoryMongoDB repository;
    private final ICustomerEntityMapper iCustomerEntityMapper;

    @Override
    public Optional<CustomerDomain> findById(String id) {
        return repository.findById(id).map(iCustomerEntityMapper::toDomain);
    }

}
