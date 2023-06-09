package com.totem.food.application.ports.in.rest;

public interface IUpdateRestApiPort<I, O> {

	O updateItem(I item);
}
