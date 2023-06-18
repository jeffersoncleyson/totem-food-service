package com.totem.food.application.usecases.category;

import com.totem.food.application.ports.in.dtos.category.CategoryFilterDto;
import com.totem.food.application.ports.out.persistence.commons.IDeleteRepositoryPort;
import com.totem.food.application.usecases.commons.IDeleteUseCase;
import com.totem.food.domain.category.CategoryDomain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DeleteCategoryUseCase implements IDeleteUseCase {

    private final IDeleteRepositoryPort<CategoryFilterDto, CategoryDomain> iDeleteRepositoryPort;

    @Override
    public void removeItem(String id) {
        iDeleteRepositoryPort.removeItem(id);
    }
}
