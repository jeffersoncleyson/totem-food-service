package com.totem.food.framework.adapters.out.persistence.mongo.combo.repository;

import lombok.SneakyThrows;
import mocks.domains.ComboDomainMock;
import mocks.models.ComboModelMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Closeable;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExistsComboRepositoryAdapterTest {

    @Mock
    private ExistsComboRepositoryAdapter.ComboRepositoryMongoDB repository;

    private ExistsComboRepositoryAdapter existsComboRepositoryAdapter;

    @Mock
    private Closeable closeable;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        existsComboRepositoryAdapter = new ExistsComboRepositoryAdapter(repository);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void exists() {

        //## Mock - Object
        var comboDomain = ComboModelMock.getMock();

        //## Given
        when(repository.existsByNameIgnoreCase(anyString())).thenReturn(true);

        //## When
        var result = existsComboRepositoryAdapter.exists(comboDomain);

        //## Then
        assertTrue(result);

    }
}