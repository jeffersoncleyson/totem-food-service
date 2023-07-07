package com.totem.food.framework.adapters.out.persistence.mongo.customer.repository;

import com.totem.food.application.ports.out.persistence.commons.ILoginRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.mapper.ICustomerEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@AllArgsConstructor
@Component
public class LoginCustomerRepositoryAdapter implements ILoginRepositoryPort<Optional<CustomerModel>> {

    @Repository
    protected interface CustomerRepositoryMongoDB extends BaseRepository<CustomerEntity, String> {
        CustomerEntity findByCpfAndPassword(String cpf, String password);
    }

    private final CustomerRepositoryMongoDB repository;
    private final ICustomerEntityMapper iCustomerEntityMapper;

    @Override
    public Optional<CustomerModel> findByCadastro(String cpf, String password) {
        var customerEntity = repository.findByCpfAndPassword(cpf, password);
        return Optional.ofNullable(iCustomerEntityMapper.toModel(customerEntity));
    }
}
