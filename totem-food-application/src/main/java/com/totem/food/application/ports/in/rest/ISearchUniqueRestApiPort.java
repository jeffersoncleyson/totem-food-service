package com.totem.food.application.ports.in.rest;

public interface ISearchUniqueRestApiPort<I, O> {

    O getById(I id);
}
