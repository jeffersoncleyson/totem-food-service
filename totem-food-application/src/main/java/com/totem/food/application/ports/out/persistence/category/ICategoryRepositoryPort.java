package com.totem.food.application.ports.out.persistence.category;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepositoryPort<T> {

    T saveItem(T item);

    T removeItem(T item);

    T updateItem(T item);

    List<T> findAll();

    Optional<T> findById(String id);
}
