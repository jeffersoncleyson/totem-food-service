package com.totem.food.application.usecases.category;

import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.dtos.category.FilterCategoryDto;
import com.totem.food.application.ports.in.mappers.category.ICategoryMapper;
import com.totem.food.application.ports.out.persistence.category.ICategoryRepositoryPort;
import com.totem.food.application.usecases.commons.ISearchUniqueUseCase;
import com.totem.food.domain.category.CategoryDomain;
import com.totem.food.domain.exceptions.ResourceNotFound;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SearchUniqueCategoryUseCase implements ISearchUniqueUseCase<String, CategoryDto> {

    private final ICategoryMapper iCategoryMapper;
    private final ICategoryRepositoryPort<FilterCategoryDto, CategoryDomain> iCategoryRepositoryPort;

    @Override
    public CategoryDto item(String id) {
        final var categoryDomain = iCategoryRepositoryPort.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFound(SearchUniqueCategoryUseCase.class, "Error searching item by identifier"));

        return iCategoryMapper.toDto(categoryDomain);
    }

}
