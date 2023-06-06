package com.totem.food.application.ports.in.rest.common;

public interface IListPort<I, O> {

	O listItem(I itemFilter);
}
