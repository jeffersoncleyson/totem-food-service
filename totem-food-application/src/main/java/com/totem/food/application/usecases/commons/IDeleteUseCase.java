package com.totem.food.application.usecases.commons;

public interface IDeleteUseCase<I, O> {

    void removeItem(I id);
}
