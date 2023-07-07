package com.totem.food.application.usecases.commons;

public interface IUpdateStatusUseCase<O> {

	O updateStatus(String id, String status);

}
