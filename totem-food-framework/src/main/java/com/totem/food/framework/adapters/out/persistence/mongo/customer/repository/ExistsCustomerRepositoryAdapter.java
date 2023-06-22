package com.totem.food.framework.adapters.out.persistence.mongo.customer.repository;

import com.totem.food.application.ports.out.persistence.commons.IExistsRepositoryPort;
import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Component
public class ExistsCustomerRepositoryAdapter implements IExistsRepositoryPort<CustomerDomain, Boolean> {
    @Repository
    protected interface CustomerRepositoryMongoDB extends BaseRepository<CustomerEntity, String> {

        boolean existsByNameIgnoreCase(String name);
    }

    private final ExistsCustomerRepositoryAdapter.CustomerRepositoryMongoDB repository;

    @Override
    public Boolean exists(CustomerDomain item) {
        return repository.existsByNameIgnoreCase(item.getName());
    }
}
