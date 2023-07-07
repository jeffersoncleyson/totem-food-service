package com.totem.food.application.usecases.commons;

public interface ISearchUniqueUseCase<I, O> {

    O item(I id);
}
