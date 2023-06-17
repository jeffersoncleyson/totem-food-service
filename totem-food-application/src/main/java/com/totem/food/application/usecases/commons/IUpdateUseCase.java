package com.totem.food.application.usecases.commons;

public interface IUpdateUseCase<I, O> {

	O updateItem(I item, String id);

}
