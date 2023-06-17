package com.totem.food.application.ports.in.rest;

public interface ISearchUnique<I, O> {

    O getById(I id);
}
