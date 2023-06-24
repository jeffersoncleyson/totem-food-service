package com.totem.food.framework.adapters.out.persistence.mongo.category.repository;

import com.totem.food.application.ports.in.dtos.category.CategoryFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
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

@AllArgsConstructor
@Component
public class SearchCategoryRepositoryAdapter implements ISearchRepositoryPort<CategoryFilterDto, List<CategoryDomain>> {

    @Repository
    protected interface CategoryRepositoryMongoDB extends BaseRepository<CategoryEntity, String> {
        @Query("{'name': ?0 }")
        List<CategoryEntity> findByFilter(String name);
    }

    private final CategoryRepositoryMongoDB repository;
    private final ICategoryEntityMapper iCategoryEntityMapper;


    @Override
    public List<CategoryDomain> findAll(CategoryFilterDto categoryFilterDto) {
        final var categorysDomain = new ArrayList<CategoryDomain>();
        if (Objects.nonNull(categoryFilterDto)) {
            repository.findByFilter(categoryFilterDto.getCategoryName()).forEach(entity -> categorysDomain.add(iCategoryEntityMapper.toDomain(entity)));
        }
        repository.findAll().forEach(entity -> categorysDomain.add(iCategoryEntityMapper.toDomain(entity)));
        return categorysDomain;
    }
}
