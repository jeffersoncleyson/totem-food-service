package com.totem.food.application.ports.out.persistence.commons;

public interface ISearchRepositoryPort<I, O> {

    O findAll(I filter);

}
