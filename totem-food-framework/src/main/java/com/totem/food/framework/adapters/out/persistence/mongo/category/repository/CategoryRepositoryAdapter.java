package com.totem.food.framework.adapters.out.persistence.mongo.category.repository;

import com.totem.food.application.ports.out.persistence.category.ICategoryRepositoryPort;
import com.totem.food.domain.category.CategoryDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.category.entity.CategoryEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.category.mapper.ICategoryEntityMapper;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Component
public class CategoryRepositoryAdapter implements ICategoryRepositoryPort<CategoryDomain> {

    @Repository
    protected interface CategoryRepositoryMongoDB extends BaseRepository<CategoryEntity, String> {
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
    public List<CategoryDomain> findAll() {
        final var categorysDomain = new ArrayList<CategoryDomain>();
        repository.findAll().forEach(element -> categorysDomain.add(iCategoryEntityMapper.toDomain(element)));
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
