package com.totem.food.application.usecases.product;

import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.dtos.product.ProductCreateDto;
import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.ports.in.mappers.category.ICategoryMapper;
import com.totem.food.application.ports.in.mappers.product.IProductMapper;
import com.totem.food.application.ports.out.persistence.category.CategoryModel;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.product.ProductModel;
import com.totem.food.domain.category.CategoryDomain;
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

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseTest {

    @Spy
    private IProductMapper iProductMapper = Mappers.getMapper(IProductMapper.class);
    @Spy
    private ICategoryMapper iCategoryMapper = Mappers.getMapper(ICategoryMapper.class);

    @Mock
    private ICreateRepositoryPort<ProductModel> iProductRepositoryPort;

    @Mock
    private ISearchUniqueRepositoryPort<Optional<CategoryModel>> iSearchRepositoryPort;

    private CreateProductUseCase createProductUseCase;
    private AutoCloseable closeable;

    @BeforeEach
    void beforeEach() {
        closeable = MockitoAnnotations.openMocks(this);
        createProductUseCase = new CreateProductUseCase(iProductMapper, iCategoryMapper, iProductRepositoryPort, iSearchRepositoryPort);
    }

    @SneakyThrows
    @AfterEach
    void closeService() {
        closeable.close();
    }

    @Test
    void createItem() {

        //### Given - Objects and Values
        final var id = UUID.randomUUID().toString();
        final var name = "Coca-cola";
        final var description = "description";
        final var image = "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg";
        final var price = 10D * (Math.random() + 1);
        final var category = "Refrigerante";
        final var now = ZonedDateTime.now(ZoneOffset.UTC);

        final var categoryId = UUID.randomUUID().toString();
        final var categoryDTO = CategoryDto.builder().id(categoryId).build();

        final var productDto = new ProductDto(
                id,
                name,
                description,
                image,
                price,
                categoryDTO,
                now,
                now
        );

        final var productCreateDto = new ProductCreateDto(
                name,
                description,
                image,
                price,
                category
        );

        final var categoryDomain = CategoryDomain.builder().id(categoryId).build();
        final var categoryModel = CategoryModel.builder().id(categoryId).build();
        final var productDomain = ProductModel.builder()
                .id(id)
                .name(name)
                .description(description)
                .image(image)
                .price(price)
                .category(categoryDomain)
                .createAt(now)
                .modifiedAt(now)
                .build();

        //### Given - Mocks
        when(iSearchRepositoryPort.findById(Mockito.anyString())).thenReturn(Optional.of(categoryModel));
        when(iProductRepositoryPort.saveItem(Mockito.any(ProductModel.class))).thenReturn(productDomain);

        //### When
        final var productDtoUseCase = createProductUseCase.createItem(productCreateDto);

        //### Then
        verify(iProductMapper, times(1)).toDomain(Mockito.any(ProductCreateDto.class));
        verify(iCategoryMapper, times(1)).toDomain(Mockito.any(CategoryModel.class));
        verify(iSearchRepositoryPort, times(1)).findById(Mockito.anyString());
        verify(iProductRepositoryPort, times(1)).saveItem(Mockito.any(ProductModel.class));
        verify(iProductMapper, times(1)).toDto(Mockito.any(ProductModel.class));

        assertThat(productDtoUseCase)
                .usingRecursiveComparison()
                .isEqualTo(productDto);

        assertNotNull(productDtoUseCase.getCreateAt());
        assertNotNull(productDtoUseCase.getModifiedAt());
    }

}