package com.totem.food.application.ports.out.persistence.category;

public interface ICategoryRepositoryPort<T> {

	public T saveItem(T item);
	public T removeItem(T item);
}
