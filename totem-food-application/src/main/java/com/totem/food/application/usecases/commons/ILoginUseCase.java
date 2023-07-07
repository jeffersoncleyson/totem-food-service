package com.totem.food.application.usecases.commons;

public interface ILoginUseCase<O> {

    O login(String id, String password);
}
