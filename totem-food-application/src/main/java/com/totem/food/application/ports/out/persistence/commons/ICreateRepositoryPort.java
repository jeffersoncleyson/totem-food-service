package com.totem.food.application.ports.out.persistence.commons;

public interface ICreateRepositoryPort<I, O> {

    O saveItem(O item);
}
