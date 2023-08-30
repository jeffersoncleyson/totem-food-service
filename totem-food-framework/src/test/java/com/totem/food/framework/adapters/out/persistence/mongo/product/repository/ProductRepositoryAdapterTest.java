package com.totem.food.framework.adapters.out.persistence.mongo.product.repository;

import com.totem.food.application.ports.out.persistence.product.ProductModel;
import com.totem.food.domain.category.CategoryDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.category.entity.CategoryEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.product.entity.ProductEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.product.mapper.IProductEntityMapper;
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

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryAdapterTest {

    @Mock
    private CreateProductRepositoryAdapter.ProductRepositoryMongoDB repository;
    @Spy
    private IProductEntityMapper iProductEntityMapper = Mappers.getMapper(IProductEntityMapper.class);

    private CreateProductRepositoryAdapter productRepositoryAdapter;
    private AutoCloseable closeable;

    @BeforeEach
    void beforeEach() {
        closeable = MockitoAnnotations.openMocks(this);
        productRepositoryAdapter = new CreateProductRepositoryAdapter(repository, iProductEntityMapper);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void saveItem() {

        //### Given - Objects and Values
        final var id = UUID.randomUUID().toString();
        final var name = "Coca-cola";
        final var description = "description";
        final var image = "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg";
        final var price = 10D * (Math.random() + 1);
        final var category = "Refrigerante";
        final var now = ZonedDateTime.now(ZoneOffset.UTC);

        final var categoryId = UUID.randomUUID().toString();
        final var categoryDomain = CategoryDomain.builder().id(categoryId).build();

        final var productModel = ProductModel.builder()
                .name(name)
                .description(description)
                .image(image)
                .price(price)
                .category(categoryDomain)
                .createAt(now)
                .modifiedAt(now)
                .build();

        final var categoryEntity = CategoryEntity.builder().id(categoryId).build();
        final var productEntity = ProductEntity.builder()
                .id(id)
                .name(name)
                .description(description)
                .image(image)
                .price(price)
                .category(categoryEntity)
                .createAt(now)
                .modifiedAt(now)
                .build();

        //### Given - Mocks
        when(repository.save(Mockito.any(ProductEntity.class))).thenReturn(productEntity);

        //### When
        final var productDomainSaved = productRepositoryAdapter.saveItem(productModel);

        //### Then
        verify(iProductEntityMapper, times(1)).toEntity(Mockito.any(ProductModel.class));
        verify(repository, times(1)).save(Mockito.any(ProductEntity.class));
        verify(iProductEntityMapper, times(1)).toModel(Mockito.any(ProductEntity.class));

        assertThat(productModel)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(productDomainSaved);

        assertNull(productModel.getId());
        assertNotNull(productDomainSaved.getId());
        assertEquals(id, productDomainSaved.getId());
    }
}