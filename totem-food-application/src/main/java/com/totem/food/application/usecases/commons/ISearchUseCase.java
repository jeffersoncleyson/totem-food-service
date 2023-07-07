package com.totem.food.application.usecases.commons;

public interface ISearchUseCase<I, O> {

    O items(I filter);

}
