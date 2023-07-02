package com.totem.food.framework.adapters.out.persistence.mongo.category.repository;

import com.totem.food.application.ports.out.persistence.category.CategoryModel;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.framework.adapters.out.persistence.mongo.category.entity.CategoryEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.category.mapper.ICategoryEntityMapper;
import lombok.SneakyThrows;
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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SearchUniqueCategoryRepositoryAdapterTest {

    @Mock
    private SearchUniqueCategoryRepositoryAdapter.CategoryRepositoryMongoDB repository;
    @Spy
    private ICategoryEntityMapper iCategoryEntityMapper = Mappers.getMapper(ICategoryEntityMapper.class);

    private ISearchUniqueRepositoryPort<Optional<CategoryModel>> iSearchUniqueRepositoryPort;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        iSearchUniqueRepositoryPort = new SearchUniqueCategoryRepositoryAdapter(repository, iCategoryEntityMapper);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void findById() {

        //### Given - Objects and Values
        final var id = UUID.randomUUID().toString();

        var categoryEntity = new CategoryEntity();
        categoryEntity.setId(id);

        //## When
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(categoryEntity));

        var categoryDomain = iSearchUniqueRepositoryPort.findById(id);

        //## Then
        assertTrue(categoryDomain.isPresent());
        assertEquals(categoryDomain.get().getId(), id);
    }
}