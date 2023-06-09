package com.totem.food.application.ports.in.rest;

public interface IRemoveRestApiPort<I, O> {

	O removeItem(I itemId);
}
