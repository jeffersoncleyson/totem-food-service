package com.totem.food.application.usecases.category;

import com.totem.food.application.ports.in.dtos.category.CategoryCreateDto;
import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.dtos.category.FilterCategoryDto;
import com.totem.food.application.ports.in.mappers.category.ICategoryMapper;
import com.totem.food.application.ports.out.persistence.category.ICategoryRepositoryPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.domain.category.CategoryDomain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CreateCategoryUseCase implements ICreateUseCase<CategoryCreateDto, CategoryDto> {

    private final ICategoryMapper iCategoryMapper;
    private final ICategoryRepositoryPort<FilterCategoryDto, CategoryDomain> iCategoryRepositoryPort;

    public CategoryDto createItem(CategoryCreateDto item) {
        final var categoryDomain = iCategoryMapper.toDomain(item);
        categoryDomain.validateCategory();
        categoryDomain.fillDates();

        if (iCategoryRepositoryPort.existsItem(categoryDomain)) {
            throw new IllegalArgumentException("Category already registered in base.");
        }

        final var categoryDomainSaved = iCategoryRepositoryPort.saveItem(categoryDomain);
        return iCategoryMapper.toDto(categoryDomainSaved);
    }

}
