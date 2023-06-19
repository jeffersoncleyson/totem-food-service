package com.totem.food.application.usecases.category;

import com.totem.food.application.ports.in.dtos.category.CategoryCreateDto;
import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.mappers.category.ICategoryMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import com.totem.food.domain.category.CategoryDomain;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
@UseCase
public class UpdateCategoryUseCase implements IUpdateUseCase<CategoryCreateDto, CategoryDto> {

    private final ICategoryMapper iCategoryMapper;
    private final IUpdateRepositoryPort<CategoryDomain> iUpdateRepositoryPort;
    private final ISearchUniqueRepositoryPort<Optional<CategoryDomain>> iSearchUniqueRepositoryPort;

    @Override
    public CategoryDto updateItem(CategoryCreateDto item, String id) {
        var categoryDomain = iSearchUniqueRepositoryPort.findById(id);

        if (categoryDomain.isPresent()) {
            categoryDomain.get().setName(item.getName());
            categoryDomain.get().validateCategory();
            categoryDomain.get().updateModifiedAt();
            final var categoryDomainUpdated = iUpdateRepositoryPort.updateItem(categoryDomain.get());
            return iCategoryMapper.toDto(categoryDomainUpdated);
        }

        //#### Implementar tratamento de erro.
        return null;
    }

}
