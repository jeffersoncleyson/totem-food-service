package com.totem.food.application.ports.out.persistence.product;

public interface IProductRepositoryPort<T> {

	public T saveItem(T item);
}
