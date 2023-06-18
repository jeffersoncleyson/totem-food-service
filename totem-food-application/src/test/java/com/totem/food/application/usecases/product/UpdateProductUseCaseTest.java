package com.totem.food.application.usecases.product;

import com.totem.food.application.ports.in.dtos.product.ProductCreateDto;
import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.ports.in.mappers.product.IProductMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UpdateProductUseCaseTest {

    @Spy
    private IProductMapper iProductMapper = Mappers.getMapper(IProductMapper.class);
    @Mock
    private IUpdateRepositoryPort<ProductDomain> iProductRepositoryPort;
    @Mock
    private ISearchUniqueRepositoryPort<ProductDomain> iSearchUniqueRepositoryPort;

    private IUpdateUseCase<ProductCreateDto, ProductDto> iUpdateUseCase;
    private AutoCloseable closeable;

    @BeforeEach
    private void beforeEach() {
        closeable = MockitoAnnotations.openMocks(this);
        iUpdateUseCase = new UpdateProductUseCase(iProductMapper, iProductRepositoryPort, iSearchUniqueRepositoryPort);
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

        final var productDto = new ProductDto(
                id,
                name,
                description,
                image,
                price,
                category,
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

        final var productDomain = ProductDomain.builder()
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
        when(iSearchUniqueRepositoryPort.findById(Mockito.anyString())).thenReturn(productDomain);
        when(iProductRepositoryPort.updateItem(Mockito.any(ProductDomain.class))).thenReturn(productDomain);

        //### When
        final var productDtoUpdated = iUpdateUseCase.updateItem(productCreateDto, id);

        //### Then
        verify(iProductMapper, times(1)).toDto(Mockito.any(ProductDomain.class));
        verify(iProductRepositoryPort, times(1)).updateItem(Mockito.any(ProductDomain.class));

        assertThat(productDtoUpdated)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(ZonedDateTime.class)
                .isEqualTo(productDto);

        assertEquals(productDto.getCreateAt(), productDtoUpdated.getCreateAt());
        assertTrue(productDtoUpdated.getModifiedAt().isAfter(productDto.getModifiedAt()));
    }
}