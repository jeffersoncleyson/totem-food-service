package com.totem.food.application.ports.out.persistence.customer;

import java.util.List;
import java.util.Optional;

public interface ICustomerRepositoryPort<T> {

    List<T> findAll();

    Optional<T> findById(String id);

}
