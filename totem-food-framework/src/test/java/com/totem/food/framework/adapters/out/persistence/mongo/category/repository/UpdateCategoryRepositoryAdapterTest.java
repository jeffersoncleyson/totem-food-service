package com.totem.food.framework.adapters.out.persistence.mongo.category.repository;

import com.totem.food.domain.category.CategoryDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.category.entity.CategoryEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.category.mapper.ICategoryEntityMapper;
import lombok.SneakyThrows;
import mocks.adapters.out.persistence.mongo.category.entity.CategoryEntityMock;
import mocks.domains.CategoryDomainMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Closeable;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCategoryRepositoryAdapterTest {

    @Mock
    private UpdateCategoryRepositoryAdapter.CategoryRepositoryMongoDB repository;

    @Spy
    private ICategoryEntityMapper iCategoryEntityMapper = Mappers.getMapper(ICategoryEntityMapper.class);

    private UpdateCategoryRepositoryAdapter updateCategoryRepositoryAdapter;

    @Mock
    private Closeable closeable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        updateCategoryRepositoryAdapter = new UpdateCategoryRepositoryAdapter(repository, iCategoryEntityMapper);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void updateItem() {

        //## Mock - Objects
        var categoryEntity = CategoryEntityMock.getMock();
        var categoryDomain = CategoryDomainMock.getMock();

        //## Given
        when(iCategoryEntityMapper.toEntity(any(CategoryDomain.class))).thenReturn(categoryEntity);
        when(repository.save(any(CategoryEntity.class))).thenReturn(categoryEntity);

        //## When
        var result = updateCategoryRepositoryAdapter.updateItem(categoryDomain);

        //## Then
        assertThat(result).usingRecursiveComparison()
                .ignoringFieldsOfTypes(ZonedDateTime.class)
                .isEqualTo(categoryDomain);
        verify(iCategoryEntityMapper, times(1)).toDomain(any(CategoryEntity.class));
    }
}