package com.totem.food.application.usecases.commons;

public interface IConfirmUseCase<O, I> {

    O confirm(I item);
}
