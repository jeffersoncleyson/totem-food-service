package com.totem.food.application.usecases.category;

import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.mappers.ICategoryMapper;
import com.totem.food.application.ports.out.persistence.category.ICategoryRepositoryPort;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.domain.category.CategoryDomain;
import com.totem.food.domain.exceptions.ResourceNotFound;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SearchCategoryUseCase implements ISearchUseCase<String, CategoryDto> {

    private final ICategoryMapper iCategoryMapper;
    private final ICategoryRepositoryPort<CategoryDomain> iCategoryRepositoryPort;

    @Override
    public List<CategoryDto> items() {
        return iCategoryRepositoryPort.findAll()
                .stream()
                .map(iCategoryMapper::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public CategoryDto item(String id) {
        final var categoryDomain = iCategoryRepositoryPort.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFound(SearchCategoryUseCase.class, "Error searching item by identifier"));

        return iCategoryMapper.toDto(categoryDomain);
    }

}
