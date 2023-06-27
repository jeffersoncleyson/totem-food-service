package com.totem.food.framework.adapters.out.persistence.mongo.product.repository;

import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.domain.product.ProductDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.product.entity.ProductEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.product.mapper.IProductEntityMapper;
import mocks.entity.ProductEntityMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchUniqueProductRepositoryAdapterTest {

    @Mock
    private SearchUniqueProductRepositoryAdapter.ProductRepositoryMongoDB repository;
    @Spy
    private IProductEntityMapper iProductEntityMapper = Mappers.getMapper(IProductEntityMapper.class);
    @Mock
    private MongoTemplate mongoTemplate;

    private ISearchUniqueRepositoryPort<Optional<ProductDomain>> iSearchUniqueRepositoryPort;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.iSearchUniqueRepositoryPort = new SearchUniqueProductRepositoryAdapter(repository, iProductEntityMapper);
    }

    @Test
    void findById() {

        //## Given
        final var productEntity = ProductEntityMock.getMock();
        when(repository.findById(anyString())).thenReturn(Optional.of(productEntity));

        //## When
        final var productDomain = iSearchUniqueRepositoryPort.findById(anyString());

        //## Then
        assertTrue(productDomain.isPresent());
        verify(iProductEntityMapper, times(1)).toDomain(any(ProductEntity.class));

    }
}