package com.totem.food.framework.adapters.out.persistence.mongo.category.repository;

import com.totem.food.application.ports.out.category.CategoryModel;
import com.totem.food.framework.adapters.out.persistence.mongo.category.entity.CategoryEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.category.mapper.ICategoryEntityMapper;
import lombok.SneakyThrows;
import mocks.adapters.out.persistence.mongo.category.entity.CategoryEntityMock;
import mocks.models.CategoryModelMock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Closeable;
import java.time.ZonedDateTime;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCategoryRepositoryAdapterTest {

    @Spy
    private ICategoryEntityMapper iCategoryEntityMapper = Mappers.getMapper(ICategoryEntityMapper.class);

    @Mock
    private CreateCategoryRepositoryAdapter.CategoryRepositoryMongoDB repository;

    private CreateCategoryRepositoryAdapter createCategoryRepositoryAdapter;

    @Mock
    private Closeable closeable;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createCategoryRepositoryAdapter = new CreateCategoryRepositoryAdapter(repository, iCategoryEntityMapper);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void saveItem() {

        //## Mock - Objects
        var categoryEntity = CategoryEntityMock.getMock();
        var categoryModel = CategoryModelMock.getMock();

        //## Given
        when(iCategoryEntityMapper.toEntity(any(CategoryModel.class))).thenReturn(categoryEntity);
        when(repository.save(any(CategoryEntity.class))).thenReturn(categoryEntity);

        //## When
        var result = createCategoryRepositoryAdapter.saveItem(categoryModel);

        //## Then
        Assertions.assertThat(result).usingRecursiveComparison().ignoringFieldsOfTypes(ZonedDateTime.class).isEqualTo(categoryModel);
        Mockito.verify(iCategoryEntityMapper, times(1)).toModel(any(CategoryEntity.class));

    }
}