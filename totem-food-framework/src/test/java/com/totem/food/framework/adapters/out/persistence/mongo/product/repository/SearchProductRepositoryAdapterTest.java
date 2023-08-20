package com.totem.food.framework.adapters.out.persistence.mongo.product.repository;

import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.product.ProductModel;
import com.totem.food.framework.adapters.out.persistence.mongo.category.entity.CategoryEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.product.entity.ProductEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.product.mapper.IProductEntityMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.bson.types.ObjectId;
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
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchProductRepositoryAdapterTest {

    @Mock
    private SearchProductRepositoryAdapter.ProductRepositoryMongoDB repository;
    @Spy
    private IProductEntityMapper iProductEntityMapper = Mappers.getMapper(IProductEntityMapper.class);
    @Mock
    private MongoTemplate mongoTemplate;

    private ISearchRepositoryPort<ProductFilterDto, List<ProductModel>> iSearchRepositoryPort;
    private AutoCloseable closeable;

    @BeforeEach
    void beforeEach() {
        closeable = MockitoAnnotations.openMocks(this);
        iSearchRepositoryPort = new SearchProductRepositoryAdapter(repository, mongoTemplate, iProductEntityMapper);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void findAllWithFilterName() {

        //### Given - Objects and Values
        final var id = UUID.randomUUID().toString();
        final var name = "Coca-cola";
        final var description = "description";
        final var image = "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg";
        final var price = 10D * (Math.random() + 1);
        final var category = "Refrigerante";
        final var now = ZonedDateTime.now(ZoneOffset.UTC);

        final var categoryId = UUID.randomUUID().toString();
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

        final var productEntityList = List.of(productEntity);

        final var productFilterDto = ProductFilterDto.builder().name(name).build();

        //### Given - Mocks
        when(repository.findByFilter(Mockito.anyString())).thenReturn(productEntityList);

        //### When
        final var productDomainList = iSearchRepositoryPort.findAll(productFilterDto);

        //### Then
        verify(iProductEntityMapper, times(1)).toModel(Mockito.any(ProductEntity.class));
        verify(repository, times(1)).findByFilter(Mockito.anyString());


        assertTrue(CollectionUtils.isNotEmpty(productDomainList));
        assertThat(productDomainList)
                .usingRecursiveComparison()
                .isEqualTo(productEntityList);
    }

    @Test
    void findAllWithFilterIds() {

        //### Given - Objects and Values
        final var id = UUID.randomUUID().toString();
        final var name = "Coca-cola";
        final var description = "description";
        final var image = "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg";
        final var price = 10D * (Math.random() + 1);
        final var category = "Refrigerante";
        final var now = ZonedDateTime.now(ZoneOffset.UTC);

        final var categoryId = UUID.randomUUID().toString();
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

        final var productEntityList = List.of(productEntity);

        final var productFilterDto = ProductFilterDto.builder().ids(List.of(productEntity.getId())).build();

        //### Given - Mocks
        when(repository.findAllByIds(Mockito.any())).thenReturn(productEntityList);

        //### When
        final var productDomainList = iSearchRepositoryPort.findAll(productFilterDto);

        //### Then
        verify(iProductEntityMapper, times(1)).toModel(Mockito.any(ProductEntity.class));
        verify(repository, times(1)).findAllByIds(Mockito.any());


        assertTrue(CollectionUtils.isNotEmpty(productDomainList));
        assertThat(productDomainList)
                .usingRecursiveComparison()
                .isEqualTo(productEntityList);
    }

    @Test
    void findAllWithFilterCategoryId() {

        //### Given - Objects and Values
        final var id = new ObjectId().toHexString();
        final var name = "Coca-cola";
        final var description = "description";
        final var image = "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg";
        final var price = 10D * (Math.random() + 1);
        final var category = "Refrigerante";
        final var now = ZonedDateTime.now(ZoneOffset.UTC);

        final var categoryId = new ObjectId().toHexString();
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

        final var productEntityList = new ArrayList<>(){{
            add(productEntity);
        }};

        final var productFilterDto = ProductFilterDto.builder().categoryId(Set.of(productEntity.getCategory().getId())).build();

        //### Given - Mocks

        when(mongoTemplate.find(Mockito.any(), Mockito.any())).thenReturn(productEntityList);

        //### When
        final var productDomainList = iSearchRepositoryPort.findAll(productFilterDto);

        //### Then
        verify(iProductEntityMapper, times(1)).toModel(Mockito.any(ProductEntity.class));
        verify(mongoTemplate, times(1)).find(Mockito.any(), Mockito.any());


        assertTrue(CollectionUtils.isNotEmpty(productDomainList));
        assertThat(productDomainList)
                .usingRecursiveComparison()
                .isEqualTo(productEntityList);
    }
}