package com.totem.food.framework.adapters.out.persistence.mongo.category.repository;

import lombok.SneakyThrows;
import mocks.domains.CategoryDomainMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Closeable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        var categoryDomain = CategoryDomainMock.getMock();

        //## Given
        when(repository.existsByNameIgnoreCase(anyString())).thenReturn(true);

        //## When
        var result = existsCategoryRepositoryAdapter.exists(categoryDomain);

        //## Then
        assertTrue(result);
    }
}