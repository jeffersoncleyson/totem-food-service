package com.totem.food.application.usecases.category;

import com.totem.food.application.ports.in.dtos.category.CategoryCreateDto;
import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.dtos.category.CategoryFilterDto;
import com.totem.food.application.ports.in.mappers.category.ICategoryMapper;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.domain.category.CategoryDomain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CreateCategoryUseCase implements ICreateUseCase<CategoryCreateDto, CategoryDto> {

    private final ICategoryMapper iCategoryMapper;
    private final ICreateRepositoryPort<CategoryFilterDto, CategoryDomain> iCreateRepositoryPort;
    private final ISearchRepositoryPort<CategoryFilterDto, CategoryDomain> iSearchRepositoryPort;

    public CategoryDto createItem(CategoryCreateDto item) {
        final var categoryDomain = iCategoryMapper.toDomain(item);
        categoryDomain.validateCategory();
        categoryDomain.fillDates();

        if (iSearchRepositoryPort.existsItem(categoryDomain)) {
            throw new IllegalArgumentException("Category already registered in base.");
        }

        final var categoryDomainSaved = iCreateRepositoryPort.saveItem(categoryDomain);
        return iCategoryMapper.toDto(categoryDomainSaved);
    }

}
