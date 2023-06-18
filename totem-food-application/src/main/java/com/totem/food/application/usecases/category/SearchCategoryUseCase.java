package com.totem.food.application.usecases.category;

import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.dtos.category.CategoryFilterDto;
import com.totem.food.application.ports.in.mappers.category.ICategoryMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.domain.category.CategoryDomain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SearchCategoryUseCase implements ISearchUseCase<CategoryFilterDto, List<CategoryDto>> {

    private final ICategoryMapper iCategoryMapper;
    private final ISearchRepositoryPort<CategoryFilterDto, List<CategoryDomain>> iSearchRepositoryPort;

    @Override
    public List<CategoryDto> items(CategoryFilterDto filter) {
        return iSearchRepositoryPort.findAll(filter)
                .stream()
                .map(iCategoryMapper::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

}
