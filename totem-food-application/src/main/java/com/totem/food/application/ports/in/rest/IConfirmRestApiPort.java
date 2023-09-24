package com.totem.food.application.ports.in.rest;

public interface IConfirmRestApiPort<I, O> {

    O confirm(I item);
}
