package com.totem.food.application.ports.out.persistence.commons;

import java.util.List;
import java.util.Optional;

public interface ISearchRepositoryPort<I, O> {

    List<O> findAll(I filter);

    Optional<O> findById(String id);

    boolean existsItem(O item);
}
