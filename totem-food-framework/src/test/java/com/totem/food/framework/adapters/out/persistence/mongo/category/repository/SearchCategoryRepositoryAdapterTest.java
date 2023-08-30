package com.totem.food.framework.adapters.out.persistence.mongo.category.repository;

import com.totem.food.application.ports.in.dtos.category.CategoryFilterDto;
import com.totem.food.framework.adapters.out.persistence.mongo.category.entity.CategoryEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.category.mapper.ICategoryEntityMapper;
import lombok.SneakyThrows;
import mocks.adapters.out.persistence.mongo.category.entity.CategoryEntityMock;
import mocks.domains.CategoryDomainMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Closeable;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchCategoryRepositoryAdapterTest {

    @Mock
    private SearchCategoryRepositoryAdapter.CategoryRepositoryMongoDB repository;

    @Spy
    private ICategoryEntityMapper iCategoryEntityMapper = Mappers.getMapper(ICategoryEntityMapper.class);

    private SearchCategoryRepositoryAdapter searchCategoryRepositoryAdapter;

    @Mock
    private Closeable closeable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        searchCategoryRepositoryAdapter = new SearchCategoryRepositoryAdapter(repository, iCategoryEntityMapper);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void findByFilter() {

        //## Mock - Objects
        var categoryFilterDto = new CategoryFilterDto("Refrigerante");
        var categoryDomain = CategoryDomainMock.getMock();
        var categoryEntity = CategoryEntityMock.getMock();

        //## Given
        when(repository.findByFilter(anyString())).thenReturn(List.of(categoryEntity));

        //## When
        var result = searchCategoryRepositoryAdapter.findAll(categoryFilterDto);

        //## Then
        Assertions.assertEquals(result.get(0).getName(), categoryDomain.getName());
        verify(iCategoryEntityMapper, times(1)).toModel(any(CategoryEntity.class));

    }

    @Test
    void findAll() {

        //## Mock - Objects
        var categoryFilterDto = new CategoryFilterDto("Refrigerante");
        var categoryDomain = CategoryDomainMock.getMock();
        var categoryEntity = CategoryEntityMock.getMock();

        //## Given
        when(repository.findAll()).thenReturn(List.of(categoryEntity));

        //## When
        var result = searchCategoryRepositoryAdapter.findAll(null);

        //## Then
        Assertions.assertEquals(result.get(0).getName(), categoryDomain.getName());
        verify(iCategoryEntityMapper, times(1)).toModel(any(CategoryEntity.class));

    }
}