package com.totem.food.application.usecases.category;

import com.totem.food.application.ports.in.dtos.category.CategoryCreateDto;
import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.mappers.ICategoryMapper;
import com.totem.food.application.ports.out.persistence.category.ICategoryRepositoryPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import com.totem.food.domain.category.CategoryDomain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UpdateCategoryUseCase implements IUpdateUseCase<CategoryCreateDto, CategoryDto> {

    private final ICategoryMapper iCategoryMapper;
    private final ICategoryRepositoryPort<CategoryDomain> iCategoryRepositoryPort;
    private final ICreateUseCase<CategoryCreateDto, CategoryDto> iCreateCategoryUseCase;

    @Override
    public CategoryDto updateItem(CategoryCreateDto item, String id) {
        var optionalDomain = iCategoryRepositoryPort.findById(id);

        if (optionalDomain.isPresent()) {
            optionalDomain.get().setName(item.getName());
            optionalDomain.get().validateCategory();
            optionalDomain.get().updateModifiedAt();
            final var categoryDomainUpdated = iCategoryRepositoryPort.updateItem(optionalDomain.get());
            return iCategoryMapper.toDto(categoryDomainUpdated);
        }

        return iCreateCategoryUseCase.createItem(item);
    }

}
