package com.totem.food.application.ports.out.persistence.commons;

public interface IRemoveRepositoryPort<O> {

    void removeItem(String id);
}
