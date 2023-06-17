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
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static mocks.adapters.out.persistence.mongo.category.entity.CategoryEntityMock.getMock;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryRepositoryAdapterTest {

    private ICategoryRepositoryPort<CategoryDomain> iCategoryRepositoryPort;
    @Spy
    private ICategoryEntityMapper iCategoryEntityMapper = Mappers.getMapper(ICategoryEntityMapper.class);
    @Mock
    private CategoryRepositoryAdapter.CategoryRepositoryMongoDB categoryRepositoryMongoDB;


    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        iCategoryRepositoryPort = new CategoryRepositoryAdapter(categoryRepositoryMongoDB, iCategoryEntityMapper);
    }

    @Test
    void saveItem() {

        //## Given
        final var categoryEntity = getMock();
        when(categoryRepositoryMongoDB.save(any(CategoryEntity.class))).thenReturn(categoryEntity);
        final var categoryDomain = iCategoryEntityMapper.toDomain(categoryEntity);
        categoryDomain.setId(null);

        //## When
        final var categoryDomainSaved = iCategoryRepositoryPort.saveItem(categoryDomain);

        //## Then
        assertThat(categoryDomainSaved).usingRecursiveComparison().isEqualTo(categoryEntity);
    }

    @Test
    void removeItem() {

        //## Given
        final var itemId = "123";

        //## When
        iCategoryRepositoryPort.removeItem(itemId);

        //## Then
        verify(categoryRepositoryMongoDB, times(1)).deleteById(itemId);
    }

    @Test
    void updateItem() {

        //## Given
        final var categoryEntity = getMock();
        when(categoryRepositoryMongoDB.save(any(CategoryEntity.class))).thenReturn(categoryEntity);
        var categoryDomain = iCategoryEntityMapper.toDomain(categoryEntity);

        //## When
        final var categoryDomainSaved = iCategoryRepositoryPort.updateItem(categoryDomain);

        //## Then
        assertThat(categoryDomainSaved).usingRecursiveComparison().isEqualTo(categoryEntity);

    }

    @Test
    void findAll() {

        //## Given
        final var categorysEntity = List.of(getMock(), getMock());
        when(categoryRepositoryMongoDB.findAll()).thenReturn(categorysEntity);

        //## When
        var listCategoryDomain = iCategoryRepositoryPort.findAll();

        //## Then
        assertThat(listCategoryDomain).usingRecursiveComparison().isEqualTo(categorysEntity);
        verify(iCategoryEntityMapper, times(2)).toDomain(any(CategoryEntity.class));

    }

    @Test
    void findById() {

        //## Given
        final var categoryEntity = getMock();
        when(categoryRepositoryMongoDB.findById(anyString())).thenReturn(Optional.of(categoryEntity));

        //## When
        var categoryDomain = iCategoryRepositoryPort.findById(anyString());

        //## Then
        assertThat(categoryDomain.get()).usingRecursiveComparison().isEqualTo(categoryEntity);
        verify(iCategoryEntityMapper, times(1)).toDomain(any(CategoryEntity.class));
    }

    @Test
    void existsItem() {

        //## Given
        final var categoryEntity = getMock();
        when(categoryRepositoryMongoDB.existsByNameIgnoreCase(anyString())).thenReturn(true);
        var domain = iCategoryEntityMapper.toDomain(categoryEntity);

        //## When
        var existItem = iCategoryRepositoryPort.existsItem(domain);

        //## Then
        assertTrue(existItem);
    }

}
