package com.totem.food.application.usecases.category;

import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.mappers.ICategoryMapper;
import com.totem.food.application.ports.out.persistence.category.ICategoryRepositoryPort;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.domain.category.CategoryDomain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class SearchCategoryUseCase implements ISearchUseCase<CategoryDto> {

    private final ICategoryMapper iCategoryMapper;
    private final ICategoryRepositoryPort<CategoryDomain> iCategoryRepositoryPort;

    @Override
    public List<CategoryDto> items() {
        var categorysDto = new ArrayList<CategoryDto>();
        var categorysDomain = iCategoryRepositoryPort.findAll();

        categorysDomain.forEach(domain -> categorysDto.add(iCategoryMapper.toDto(domain)));

        return categorysDto;
    }
}
