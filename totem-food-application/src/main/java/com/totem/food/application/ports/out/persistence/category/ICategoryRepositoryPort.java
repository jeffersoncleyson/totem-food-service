package com.totem.food.application.ports.out.persistence.category;

import java.util.List;

public interface ICategoryRepositoryPort<T> {

    T saveItem(T item);

    T removeItem(T item);

    T updateItem(T item);

    List<T> findAll();
}
