package com.totem.food.application.ports.out.persistence.commons;

public interface ILoginRepositoryPort<O> {

    O findByCadastro(String id, String password);
}
