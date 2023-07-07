package com.totem.food.application.usecases.product;

import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.dtos.product.ProductCreateDto;
import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.ports.in.mappers.category.ICategoryMapper;
import com.totem.food.application.ports.in.mappers.product.IProductMapper;
import com.totem.food.application.ports.out.persistence.category.CategoryModel;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.ports.out.persistence.product.ProductModel;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import com.totem.food.domain.category.CategoryDomain;
import com.totem.food.domain.product.ProductDomain;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateProductUseCaseTest {

    @Spy
    private IProductMapper iProductMapper = Mappers.getMapper(IProductMapper.class);
    @Spy
    private ICategoryMapper iCategoryMapper = Mappers.getMapper(ICategoryMapper.class);
    @Mock
    private IUpdateRepositoryPort<ProductModel> iProductRepositoryPort;
    @Mock
    private ISearchUniqueRepositoryPort<Optional<ProductModel>> iSearchUniqueRepositoryPort;
    @Mock
    private ISearchUniqueRepositoryPort<Optional<CategoryModel>> iSearchUniqueCategoryRepositoryPort;

    private IUpdateUseCase<ProductCreateDto, ProductDto> iUpdateUseCase;
    private AutoCloseable closeable;

    @BeforeEach
    void beforeEach() {
        closeable = MockitoAnnotations.openMocks(this);
        iUpdateUseCase = new UpdateProductUseCase(iProductMapper, iCategoryMapper, iProductRepositoryPort, iSearchUniqueRepositoryPort, iSearchUniqueCategoryRepositoryPort);
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

        final var categoryId = UUID.randomUUID().toString();
        final var categoryDomain = CategoryDomain.builder().id(categoryId).build();
        final var categoryModel = CategoryModel.builder().id(categoryId).build();

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

        final var productModel = ProductModel.builder()
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
        when(iSearchUniqueRepositoryPort.findById(Mockito.anyString())).thenReturn(Optional.of(productModel));
        when(iSearchUniqueCategoryRepositoryPort.findById(Mockito.anyString())).thenReturn(Optional.of(categoryModel));
        when(iProductRepositoryPort.updateItem(Mockito.any(ProductModel.class))).thenReturn(productModel);

        //### When
        final var productDtoUpdated = iUpdateUseCase.updateItem(productCreateDto, id);

        //### Then
        verify(iProductMapper, times(1)).toDto(Mockito.any(ProductModel.class));
        verify(iCategoryMapper, times(1)).toDomain(Mockito.any(CategoryModel.class));
        verify(iProductRepositoryPort, times(1)).updateItem(Mockito.any(ProductModel.class));

        assertThat(productDtoUpdated)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(ZonedDateTime.class)
                .isEqualTo(productDto);

        assertEquals(productDto.getCreateAt(), productDtoUpdated.getCreateAt());
    }
}