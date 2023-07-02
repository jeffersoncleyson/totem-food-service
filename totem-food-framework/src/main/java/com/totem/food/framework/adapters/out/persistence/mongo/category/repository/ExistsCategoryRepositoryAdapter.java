package com.totem.food.framework.adapters.out.persistence.mongo.category.repository;

import com.totem.food.application.ports.out.category.CategoryModel;
import com.totem.food.application.ports.out.persistence.commons.IExistsRepositoryPort;
import com.totem.food.domain.category.CategoryDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.category.entity.CategoryEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Component
public class ExistsCategoryRepositoryAdapter implements IExistsRepositoryPort<CategoryModel, Boolean> {


    @Repository
    protected interface CategoryRepositoryMongoDB extends BaseRepository<CategoryEntity, String> {

        boolean existsByNameIgnoreCase(String name);
    }

    private final CategoryRepositoryMongoDB repository;

    @Override
    public Boolean exists(CategoryModel item) {
        return repository.existsByNameIgnoreCase(item.getName());
    }
}
