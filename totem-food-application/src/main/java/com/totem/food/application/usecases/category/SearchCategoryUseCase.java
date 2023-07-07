package com.totem.food.application.usecases.category;

import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.dtos.category.CategoryFilterDto;
import com.totem.food.application.ports.in.mappers.category.ICategoryMapper;
import com.totem.food.application.ports.out.persistence.category.CategoryModel;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@UseCase
public class SearchCategoryUseCase implements ISearchUseCase<CategoryFilterDto, List<CategoryDto>> {

    private final ICategoryMapper iCategoryMapper;
    private final ISearchRepositoryPort<CategoryFilterDto, List<CategoryModel>> iSearchRepositoryPort;

    @Override
    public List<CategoryDto> items(CategoryFilterDto filter) {
        return iSearchRepositoryPort.findAll(filter)
                .stream()
                .map(iCategoryMapper::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
