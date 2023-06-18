package com.totem.food.application.ports.out.persistence.commons;

public interface ICreateRepositoryPort<O> {

    O saveItem(O item);
}
