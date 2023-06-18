package com.totem.food.application.usecases.commons;

public interface IDeleteUseCase<I, O> {

    O removeItem(I id);
}
