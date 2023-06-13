package com.totem.food.application.ports.in.rest;

import java.util.List;

public interface ISearchRestApiPort<I, O> {

	List<O> listAllCategories();
	O getCategoryByID(I id);
}
