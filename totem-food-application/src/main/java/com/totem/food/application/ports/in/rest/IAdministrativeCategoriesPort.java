package com.totem.food.application.ports.in.rest;

public interface IAdministrativeCategoriesPort<I, O> {

	O createItem(I item);
	O removeItem(I item);
	O updateItem(I item);

}
