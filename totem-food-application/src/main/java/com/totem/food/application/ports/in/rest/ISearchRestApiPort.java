package com.totem.food.application.ports.in.rest;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ISearchRestApiPort<I, O> {

    ResponseEntity<List<O>> listAll();

	ResponseEntity<O> getById(I id);
}
