package com.totem.food.application.ports.in.rest;

public interface ISearchRestApiPort<I, O> {

    O listAll(I filter);

}
