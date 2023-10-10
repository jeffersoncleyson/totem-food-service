package com.totem.food.application.ports.in.rest;

public interface ISearchImageApiPort<O> {

    O getImage(String id, boolean condition);
}
