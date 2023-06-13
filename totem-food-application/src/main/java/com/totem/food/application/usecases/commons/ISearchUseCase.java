package com.totem.food.application.usecases.commons;

import java.util.List;

public interface ISearchUseCase<I, O> {

    List<O> items();

    O item(I id);
}
