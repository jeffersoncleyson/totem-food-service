package com.totem.food.application.ports.out.persistence.category;

import java.util.List;
import java.util.Optional;

public interface ICategoryRepositoryPort<I, O> {

    O saveItem(O item);

    void removeItem(String item);

    O updateItem(O item);

    List<O> findAll(I filter);

    Optional<O> findById(String id);

    boolean existsItem(O item);
}
