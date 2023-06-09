package com.totem.food.framework.adapters.out.persistence.mongo.category.repository;

import com.totem.food.application.ports.out.persistence.category.ICategoryRepositoryPort;
import com.totem.food.domain.category.CategoryDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.category.entity.CategoryEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.category.mapper.ICategoryEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryRepositoryAdapterTest {

    private ICategoryRepositoryPort<CategoryDomain> iCategoryRepositoryPort;
    @Spy
    private ICategoryEntityMapper iCategoryEntityMapper = Mappers.getMapper(ICategoryEntityMapper.class);
    @Mock
    private CategoryRepositoryAdapter.CategoryRepositoryMongoDB categoryRepositoryMongoDB;


    @BeforeEach
    private void beforeEach() {
        MockitoAnnotations.openMocks(this);
        iCategoryRepositoryPort = new CategoryRepositoryAdapter(categoryRepositoryMongoDB, iCategoryEntityMapper);
    }

    @Test
    void saveItem() {

        //## Given
        final var categoryEntity = new CategoryEntity("123", "Name", ZonedDateTime.now(ZoneOffset.UTC), ZonedDateTime.now(ZoneOffset.UTC));
        when(categoryRepositoryMongoDB.save(Mockito.any(CategoryEntity.class))).thenReturn(categoryEntity);
        final var categoryDomain = iCategoryEntityMapper.toDomain(categoryEntity);
        categoryDomain.setId(null);

        //## When
        final var categoryDomainSaved = iCategoryRepositoryPort.saveItem(categoryDomain);

        //## Then
        assertThat(categoryDomainSaved).usingRecursiveComparison().isEqualTo(categoryEntity);

    }
}