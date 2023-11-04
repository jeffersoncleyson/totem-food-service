package com.totem.food.application.usecases.commons;

public interface ICreateWithIdentifierUseCase<I, O> {

	O createItem(I item, String identifier);

}
