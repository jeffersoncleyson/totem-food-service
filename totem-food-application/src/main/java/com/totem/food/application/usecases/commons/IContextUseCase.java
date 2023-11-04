package com.totem.food.application.usecases.commons;

public interface IContextUseCase<I, O> {

    void setContext(I value);
    O getContext();
    void clearContext();

}
