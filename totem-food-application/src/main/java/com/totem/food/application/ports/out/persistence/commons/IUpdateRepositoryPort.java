package com.totem.food.application.ports.out.persistence.commons;

public interface IUpdateRepositoryPort<I, O> {
    O updateItem(O item);
}
