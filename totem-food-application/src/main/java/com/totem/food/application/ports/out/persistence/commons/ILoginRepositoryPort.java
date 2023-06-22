package com.totem.food.application.ports.out.persistence.commons;

public interface ILoginRepositoryPort<O> {

    O findByCadastre(String id, String password);
}
