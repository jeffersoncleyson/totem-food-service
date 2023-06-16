package com.totem.food.application.ports.in.rest;

import org.springframework.http.ResponseEntity;

public interface ICreateRestApiPort<I, O> {

    ResponseEntity<O> createCategory(I item);
}
