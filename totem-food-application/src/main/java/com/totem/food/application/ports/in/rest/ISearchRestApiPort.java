package com.totem.food.application.ports.in.rest;

import java.util.List;

public interface ISearchRestApiPort<I, O> {

	List<O> getItems();
	O getItem(I id);
}
