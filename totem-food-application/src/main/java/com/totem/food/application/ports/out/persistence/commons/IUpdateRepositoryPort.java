package com.totem.food.application.ports.out.persistence.commons;

public interface IUpdateRepositoryPort<O> {

	O updateItem(O item);

}
