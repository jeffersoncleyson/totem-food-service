package com.totem.food.application.ports.out.persistence.commons;

public interface ISearchUniqueRepositoryPort<I, O> {

    O findById(I id);
}
