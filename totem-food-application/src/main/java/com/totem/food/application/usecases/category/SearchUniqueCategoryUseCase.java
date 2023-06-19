package com.totem.food.application.usecases.category;

import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.mappers.category.ICategoryMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.ISearchUniqueUseCase;
import com.totem.food.domain.category.CategoryDomain;
import com.totem.food.domain.exceptions.ResourceNotFound;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
@UseCase
public class SearchUniqueCategoryUseCase implements ISearchUniqueUseCase<String, CategoryDto> {

    private final ICategoryMapper iCategoryMapper;
    private final ISearchUniqueRepositoryPort<Optional<CategoryDomain>> iSearchRepositoryPort;

    @Override
    public CategoryDto item(String id) {
        final var categoryDomain = iSearchRepositoryPort.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFound(SearchUniqueCategoryUseCase.class, "ResponseError searching item by identifier"));

        return iCategoryMapper.toDto(categoryDomain);
    }

}
