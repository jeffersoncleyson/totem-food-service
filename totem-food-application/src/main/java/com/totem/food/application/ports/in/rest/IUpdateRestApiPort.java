package com.totem.food.application.ports.in.rest;

import org.springframework.http.ResponseEntity;

public interface IUpdateRestApiPort<I, O> {

    ResponseEntity<O> updateCategory(I item, String id);
}
