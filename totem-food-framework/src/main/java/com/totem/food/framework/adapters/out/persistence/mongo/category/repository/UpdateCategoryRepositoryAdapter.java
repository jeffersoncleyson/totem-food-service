package com.totem.food.framework.adapters.out.persistence.mongo.category.repository;

import com.totem.food.application.ports.in.dtos.category.CategoryFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.domain.category.CategoryDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.category.entity.CategoryEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.category.mapper.ICategoryEntityMapper;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Component
public class UpdateCategoryRepositoryAdapter implements IUpdateRepositoryPort<CategoryFilterDto, CategoryDomain> {

    @Repository
    protected interface CategoryRepositoryMongoDB extends BaseRepository<CategoryEntity, String> {

    }

    private final CategoryRepositoryMongoDB repository;
    private final ICategoryEntityMapper iCategoryEntityMapper;

    @Override
    public CategoryDomain updateItem(CategoryDomain item) {
        final var categoryEntity = iCategoryEntityMapper.toEntity(item);
        final var savedCategoryEntity = repository.save(categoryEntity);
        return iCategoryEntityMapper.toDomain(savedCategoryEntity);
    }
}
