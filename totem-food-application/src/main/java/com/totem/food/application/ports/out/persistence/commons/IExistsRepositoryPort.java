package com.totem.food.application.ports.out.persistence.commons;

public interface IExistsRepositoryPort<I, O> {

	O exists(I item);
}
