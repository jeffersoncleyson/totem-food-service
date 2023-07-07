package com.totem.food.application.ports.out.email;

public interface ISendEmailPort<I, O> {

    O sendEmail(I item);
}
