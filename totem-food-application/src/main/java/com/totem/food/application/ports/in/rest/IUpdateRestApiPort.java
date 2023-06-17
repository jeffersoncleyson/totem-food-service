package com.totem.food.application.ports.in.rest;

import org.springframework.http.ResponseEntity;

public interface IUpdateRestApiPort<I, O> {

    ResponseEntity<O> update(I item, String id);
}
