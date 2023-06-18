package com.totem.food.framework.adapters.out.persistence.mongo.product.repository;

import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.domain.product.ProductDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.product.entity.ProductEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.product.mapper.IProductEntityMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateProductRepositoryAdapterTest {

    @Mock
    private UpdateProductRepositoryAdapter.ProductRepositoryMongoDB repository;
    @Spy
    private IProductEntityMapper iProductEntityMapper = Mappers.getMapper(IProductEntityMapper.class);

    private IUpdateRepositoryPort<ProductDomain> iUpdateRepositoryPort;
    private AutoCloseable closeable;

    @BeforeEach
    private void beforeEach() {
        closeable = MockitoAnnotations.openMocks(this);
        iUpdateRepositoryPort = new UpdateProductRepositoryAdapter(repository, iProductEntityMapper);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void updateItem() {

        //### Given - Objects and Values
        final var id = UUID.randomUUID().toString();
        final var name = "Coca-cola";
        final var description = "description";
        final var image = "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg";
        final var price = 10D * (Math.random() + 1);
        final var category = "Refrigerante";
        final var now = ZonedDateTime.now(ZoneOffset.UTC);

        final var productDomain = ProductDomain.builder()
                .name(name)
                .description(description)
                .image(image)
                .price(price)
                .category(category)
                .createAt(now)
                .modifiedAt(now)
                .build();

        final var productEntity = ProductEntity.builder()
                .id(id)
                .name(name)
                .description(description)
                .image(image)
                .price(price)
                .category(category)
                .createAt(now)
                .modifiedAt(now)
                .build();

        //### Given - Mocks
        when(repository.save(Mockito.any(ProductEntity.class))).thenReturn(productEntity);

        //### When
        final var productDomainSaved = iUpdateRepositoryPort.updateItem(productDomain);

        //### Then
        verify(iProductEntityMapper, times(1)).toEntity(Mockito.any(ProductDomain.class));
        verify(repository, times(1)).save(Mockito.any(ProductEntity.class));
        verify(iProductEntityMapper, times(1)).toDomain(Mockito.any(ProductEntity.class));

        assertThat(productDomain)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(productDomainSaved);

        assertNull(productDomain.getId());
        assertNotNull(productDomainSaved.getId());
        assertEquals(id, productDomainSaved.getId());
    }
}