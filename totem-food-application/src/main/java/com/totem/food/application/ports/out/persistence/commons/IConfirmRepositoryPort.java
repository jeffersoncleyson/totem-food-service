package com.totem.food.application.ports.out.persistence.commons;

public interface IConfirmRepositoryPort<I, O> {

    O confirmItem(I item);
}
