package com.totem.food.application.usecases.category;

import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.out.persistence.category.ICategoryRepositoryPort;
import com.totem.food.application.usecases.commons.IDeleteUseCase;
import com.totem.food.domain.category.CategoryDomain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class DeleteCategoryUseCase implements IDeleteUseCase<String, CategoryDto> {

    private final ICategoryRepositoryPort<CategoryDomain> iCategoryRepositoryPort;

    @Override
    public CategoryDto removeItem(String id) {
        iCategoryRepositoryPort.removeItem(id);
        return null;
    }
}
