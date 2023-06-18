package com.totem.food.framework.adapters.out.persistence.mongo.category.repository;

import com.totem.food.application.ports.in.dtos.category.CategoryFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IDeleteRepositoryPort;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Component
public class CategoryRepositoryAdapter implements ICreateRepositoryPort<CategoryFilterDto, CategoryDomain>,
        ISearchRepositoryPort<CategoryFilterDto, CategoryDomain>,
        IDeleteRepositoryPort<CategoryFilterDto, CategoryDomain>,
        IUpdateRepositoryPort<CategoryFilterDto, CategoryDomain> {

    @Repository
    protected interface CategoryRepositoryMongoDB extends BaseRepository<CategoryEntity, String> {

        @Query("{'name': ?0 }")
        List<CategoryEntity> findByFilter(String name);

        boolean existsByNameIgnoreCase(String name);
    }

    private final CategoryRepositoryMongoDB repository;
    private final ICategoryEntityMapper iCategoryEntityMapper;

    @Override
    public CategoryDomain saveItem(CategoryDomain item) {
        final var categoryEntity = iCategoryEntityMapper.toEntity(item);
        final var savedCategoryEntity = repository.save(categoryEntity);
        return iCategoryEntityMapper.toDomain(savedCategoryEntity);
    }

    @Override
    public void removeItem(String itemId) {
        repository.deleteById(itemId);
    }

    @Override
    public CategoryDomain updateItem(CategoryDomain item) {
        return saveItem(item);
    }

    @Override
    public List<CategoryDomain> findAll(CategoryFilterDto categoryFilterDto) {
        final var categorysDomain = new ArrayList<CategoryDomain>();
        if (Objects.nonNull(categoryFilterDto)) {
            repository.findByFilter(categoryFilterDto.getCategoryName()).forEach(entity -> categorysDomain.add(iCategoryEntityMapper.toDomain(entity)));
        }
        repository.findAll().forEach(entity -> categorysDomain.add(iCategoryEntityMapper.toDomain(entity)));
        return categorysDomain;
    }

    @Override
    public Optional<CategoryDomain> findById(String id) {
        return repository.findById(id).map(iCategoryEntityMapper::toDomain);
    }

    @Override
    public boolean existsItem(CategoryDomain item) {
        return repository.existsByNameIgnoreCase(item.getName());
    }
}
