package com.totem.food.application.usecases.category;

import com.totem.food.application.exceptions.ElementExistsException;
import com.totem.food.application.ports.in.dtos.category.CategoryCreateDto;
import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.mappers.category.ICategoryMapper;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IExistsRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.domain.category.CategoryDomain;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@UseCase
public class CreateCategoryUseCase implements ICreateUseCase<CategoryCreateDto, CategoryDto> {

    private final ICategoryMapper iCategoryMapper;
    private final ICreateRepositoryPort<CategoryDomain> iCreateRepositoryPort;
    private final IExistsRepositoryPort<CategoryDomain, Boolean> iSearchRepositoryPort;

    public CategoryDto createItem(CategoryCreateDto item) {
        final var categoryDomain = iCategoryMapper.toDomain(item);
        categoryDomain.validateCategory();
        categoryDomain.fillDates();

        if (Boolean.TRUE.equals(iSearchRepositoryPort.exists(categoryDomain))) {
            throw new ElementExistsException(String.format("Category [%s] already registered", item.getName()));
        }

        final var categoryDomainSaved = iCreateRepositoryPort.saveItem(categoryDomain);
        return iCategoryMapper.toDto(categoryDomainSaved);
    }

}
