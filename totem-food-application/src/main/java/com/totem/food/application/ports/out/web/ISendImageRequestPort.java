package com.totem.food.application.ports.out.web;

public interface ISendImageRequestPort<I> {

    byte[] sendRequest(I item);
}
