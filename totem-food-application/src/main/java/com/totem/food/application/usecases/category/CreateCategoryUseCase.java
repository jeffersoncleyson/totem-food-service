package com.totem.food.application.usecases.category;

import com.totem.food.application.exceptions.ElementExistsException;
import com.totem.food.application.ports.in.dtos.category.CategoryCreateDto;
import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.mappers.category.ICategoryMapper;
import com.totem.food.application.ports.out.category.CategoryModel;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IExistsRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@UseCase
public class CreateCategoryUseCase implements ICreateUseCase<CategoryCreateDto, CategoryDto> {

    private final ICategoryMapper iCategoryMapper;
    private final ICreateRepositoryPort<CategoryModel> iCreateRepositoryPort;
    private final IExistsRepositoryPort<CategoryModel, Boolean> iSearchRepositoryPort;

    public CategoryDto createItem(CategoryCreateDto item) {
        final var categoryDomain = iCategoryMapper.toDomain(item);
        categoryDomain.validateCategory();
        categoryDomain.fillDates();

        final var model = iCategoryMapper.toModel(categoryDomain);
        if (Boolean.TRUE.equals(iSearchRepositoryPort.exists(model))) {
            throw new ElementExistsException(String.format("Category [%s] already registered", item.getName()));
        }
        final var categoryDomainSaved = iCreateRepositoryPort.saveItem(model);
        return iCategoryMapper.toDto(categoryDomainSaved);
    }

}
