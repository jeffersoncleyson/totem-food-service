package com.totem.food.application.usecases.category;

import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.out.persistence.category.CategoryModel;
import com.totem.food.application.ports.out.persistence.commons.IDeleteRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.IDeleteUseCase;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@UseCase
public class DeleteCategoryUseCase implements IDeleteUseCase<String, CategoryDto> {

    private final IDeleteRepositoryPort<String, CategoryModel> iDeleteRepositoryPort;

    @Override
    public CategoryDto removeItem(String id) {
        iDeleteRepositoryPort.removeItem(id);
        return null;
    }
}
