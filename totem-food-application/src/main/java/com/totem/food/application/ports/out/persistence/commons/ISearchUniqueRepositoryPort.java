package com.totem.food.application.ports.out.persistence.commons;

public interface ISearchUniqueRepositoryPort<O> {

    O findById(String id);

}
