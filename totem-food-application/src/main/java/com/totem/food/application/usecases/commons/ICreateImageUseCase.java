package com.totem.food.application.usecases.commons;

public interface ICreateImageUseCase<I, O> {

    O createImage(I data);
}
