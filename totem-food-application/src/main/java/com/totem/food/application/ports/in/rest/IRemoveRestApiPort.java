package com.totem.food.application.ports.in.rest;

import org.springframework.http.ResponseEntity;

public interface IRemoveRestApiPort<I, O> {

	ResponseEntity<Void> deleteById(I itemId);
}
