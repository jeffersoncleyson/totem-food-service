package com.totem.food.framework.adapters.out.persistence.mongo.category.repository;

import lombok.SneakyThrows;
import mocks.models.CategoryModelMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Closeable;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExistsCategoryRepositoryAdapterTest {

    @Mock
    private ExistsCategoryRepositoryAdapter.CategoryRepositoryMongoDB repository;

    private ExistsCategoryRepositoryAdapter existsCategoryRepositoryAdapter;

    @Mock
    private Closeable closeable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        existsCategoryRepositoryAdapter = new ExistsCategoryRepositoryAdapter(repository);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void exists() {

        //## Mock - Object
        var categoryModel = CategoryModelMock.getMock();

        //## Given
        when(repository.existsByNameIgnoreCase(anyString())).thenReturn(true);

        //## When
        var result = existsCategoryRepositoryAdapter.exists(categoryModel);

        //## Then
        assertTrue(result);
    }
}