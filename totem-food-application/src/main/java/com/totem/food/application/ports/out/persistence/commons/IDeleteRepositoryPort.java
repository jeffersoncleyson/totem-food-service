package com.totem.food.application.ports.out.persistence.commons;

public interface IDeleteRepositoryPort<I, O> {

    void removeItem(String item);
}
