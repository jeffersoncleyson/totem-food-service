package com.totem.food.application.usecases.commons;

public interface ICreateUseCase<I, O> {

	O createItem(I item);

}
