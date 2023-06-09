package com.totem.food.application.ports.in.rest;

public interface ICreateRestApiPort<I, O> {

	O createItem(I item);
}
