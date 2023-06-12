package com.totem.food.application.usecases.commons;

import java.util.List;

public interface ISearchUseCase<O> {

    List<O> items();
}
