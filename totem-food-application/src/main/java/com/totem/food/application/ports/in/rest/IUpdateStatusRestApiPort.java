package com.totem.food.application.ports.in.rest;

import java.util.Map;

public interface IUpdateStatusRestApiPort<O> {

    O update(String id, String status);
}
