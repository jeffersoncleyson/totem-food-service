package com.totem.food.application.usecases.category;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.category.CategoryCreateDto;
import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.mappers.category.ICategoryMapper;
import com.totem.food.application.ports.out.persistence.category.CategoryModel;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
@UseCase
public class UpdateCategoryUseCase implements IUpdateUseCase<CategoryCreateDto, CategoryDto> {

    private final ICategoryMapper iCategoryMapper;
    private final IUpdateRepositoryPort<CategoryModel> iUpdateRepositoryPort;
    private final ISearchUniqueRepositoryPort<Optional<CategoryModel>> iSearchUniqueRepositoryPort;

    @Override
    public CategoryDto updateItem(CategoryCreateDto item, String id) {
        var categoryModel = iSearchUniqueRepositoryPort.findById(id);

        if (categoryModel.isPresent()) {

            final var categoryDomain = iCategoryMapper.toDomain(categoryModel.get());
            categoryDomain.setName(item.getName());
            categoryDomain.validateCategory();
            categoryDomain.updateModifiedAt();
            final var model = iCategoryMapper.toModel(categoryDomain);
            final var categoryModelUpdated = iUpdateRepositoryPort.updateItem(model);
            return iCategoryMapper.toDto(categoryModelUpdated);
        }

        throw new ElementNotFoundException(String.format("Category [%s] not found", id));
    }

}
