package com.totem.food.application.ports.in.rest;

public interface IUpdateStatusRestApiPort<O> {

    O update(String id, String status);
}
