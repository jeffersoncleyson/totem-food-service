package com.totem.food.framework.adapters.out.persistence.mongo.category.repository;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Closeable;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RemoveCategoryRepositoryAdapterTest {

    @Mock
    private RemoveCategoryRepositoryAdapter.CategoryRepositoryMongoDB repositoy;

    private RemoveCategoryRepositoryAdapter removeCategoryRepositoryAdapter;

    @Mock
    private Closeable closeable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        removeCategoryRepositoryAdapter = new RemoveCategoryRepositoryAdapter(repositoy);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void removeItem() {

        //## When
        removeCategoryRepositoryAdapter.removeItem(anyString());

        //## Then
        verify(repositoy, times(1)).deleteById(anyString());
    }
}