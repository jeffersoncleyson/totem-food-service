package com.totem.food.application.ports.in.rest;

public interface IUpdateRestApiPort<I, O> {

	O updateCategory(I item, String id);
}
