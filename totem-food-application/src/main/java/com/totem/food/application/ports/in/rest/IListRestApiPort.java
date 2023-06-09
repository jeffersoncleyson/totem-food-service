package com.totem.food.application.ports.in.rest;

public interface IListRestApiPort<I, O> {

	O listItem(I itemFilter);
}
