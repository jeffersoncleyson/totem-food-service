package com.totem.food.framework.adapters.out.persistence.mongo.category.repository;

import com.totem.food.application.ports.in.dtos.category.CategoryFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IDeleteRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryRepositoryAdapterTest {

    private ICreateRepositoryPort<CategoryFilterDto, CategoryDomain> iCreateRepositoryPort;
    private ISearchRepositoryPort<CategoryFilterDto, CategoryDomain> iSearchRepositoryPort;
    private IDeleteRepositoryPort<CategoryFilterDto, CategoryDomain> iDeleteRepositoryPort;
    private IUpdateRepositoryPort<CategoryFilterDto, CategoryDomain> iUpdateRepositoryPort;
    @Spy
    private ICategoryEntityMapper iCategoryEntityMapper = Mappers.getMapper(ICategoryEntityMapper.class);
    @Mock
    private CategoryRepositoryAdapter.CategoryRepositoryMongoDB repository;


    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        iCreateRepositoryPort = new CategoryRepositoryAdapter(repository, iCategoryEntityMapper);
        iSearchRepositoryPort = new CategoryRepositoryAdapter(repository, iCategoryEntityMapper);
        iDeleteRepositoryPort = new CategoryRepositoryAdapter(repository, iCategoryEntityMapper);
        iUpdateRepositoryPort = new CategoryRepositoryAdapter(repository, iCategoryEntityMapper);
    }

    @Test
    void saveItem() {

        //## Given
        final var categoryEntity = getMock();
        when(repository.save(any(CategoryEntity.class))).thenReturn(categoryEntity);
        final var categoryDomain = iCategoryEntityMapper.toDomain(categoryEntity);
        categoryDomain.setId(null);

        //## When
        final var categoryDomainSaved = iCreateRepositoryPort.saveItem(categoryDomain);

        //## Then
        assertThat(categoryDomainSaved).usingRecursiveComparison().isEqualTo(categoryEntity);
    }

    @Test
    void removeItem() {

        //## Given
        final var itemId = "123";

        //## When
        iDeleteRepositoryPort.removeItem(itemId);

        //## Then
        verify(repository, times(1)).deleteById(itemId);
    }

    @Test
    void updateItem() {

        //## Given
        final var categoryEntity = getMock();
        when(repository.save(any(CategoryEntity.class))).thenReturn(categoryEntity);
        var categoryDomain = iCategoryEntityMapper.toDomain(categoryEntity);

        //## When
        final var categoryDomainSaved = iUpdateRepositoryPort.updateItem(categoryDomain);

        //## Then
        assertThat(categoryDomainSaved).usingRecursiveComparison().isEqualTo(categoryEntity);

    }

    @Test
    void findAll() {

        //## Given
        final var categorysEntity = List.of(getMock(), getMock());
        final var categoryFilter = new CategoryFilterDto("Name");
        when(repository.findByFilter(anyString())).thenReturn(categorysEntity);

        //## When
        var listCategoryDomain = iSearchRepositoryPort.findAll(categoryFilter);

        //## Then
        assertThat(listCategoryDomain).usingRecursiveComparison().isEqualTo(categorysEntity);
        verify(iCategoryEntityMapper, times(2)).toDomain(any(CategoryEntity.class));

    }

    @Test
    void findById() {

        //## Given
        final var categoryEntity = getMock();
        when(repository.findById(anyString())).thenReturn(Optional.of(categoryEntity));

        //## When
        var categoryDomain = iSearchRepositoryPort.findById(anyString());

        //## Then
        assertThat(categoryDomain.get()).usingRecursiveComparison().isEqualTo(categoryEntity);
        verify(iCategoryEntityMapper, times(1)).toDomain(any(CategoryEntity.class));
    }

    @Test
    void existsItem() {

        //## Given
        final var categoryEntity = getMock();
        when(repository.existsByNameIgnoreCase(anyString())).thenReturn(true);
        var domain = iCategoryEntityMapper.toDomain(categoryEntity);

        //## When
        var existItem = iSearchRepositoryPort.existsItem(domain);

        //## Then
        assertTrue(existItem);
    }

}
