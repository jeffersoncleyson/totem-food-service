package com.totem.food.application.ports.out.web;

public interface ISendRequestPort<I, O> {

    String IDEMPOTENCE_KEY = "x-idempotence-key";

    O sendRequest(I item);
}
