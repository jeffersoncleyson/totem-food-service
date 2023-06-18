package com.totem.food.framework.adapters.out.persistence.mongo.category.repository;

import com.totem.food.application.ports.in.dtos.category.CategoryFilterDto;
import com.totem.food.application.ports.out.persistence.commons.IDeleteRepositoryPort;
import com.totem.food.domain.category.CategoryDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.category.entity.CategoryEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.category.mapper.ICategoryEntityMapper;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Component
public class RemoveCategoryRepositoryAdapter implements IDeleteRepositoryPort<CategoryFilterDto, CategoryDomain> {

    @Repository
    protected interface CategoryRepositoryMongoDB extends BaseRepository<CategoryEntity, String> {

    }

    private final CategoryRepositoryMongoDB repository;


    @Override
    public void removeItem(String itemId) {
        repository.deleteById(itemId);
    }

}
