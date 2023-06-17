package com.totem.food.application.ports.in.rest;

public interface IUpdateRestApiPort<I, O> {

    O update(I item, String id);
}
