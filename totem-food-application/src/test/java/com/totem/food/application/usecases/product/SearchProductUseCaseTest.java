package com.totem.food.application.usecases.product;

import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
import com.totem.food.application.ports.in.mappers.product.IProductMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.product.ProductModel;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.domain.category.CategoryDomain;
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
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchProductUseCaseTest {

    @Spy
    private IProductMapper iProductMapper = Mappers.getMapper(IProductMapper.class);
    @Mock
    private ISearchRepositoryPort<ProductFilterDto, List<ProductModel>> iSearchProductRepositoryPort;
    private ISearchUseCase<ProductFilterDto, List<ProductDto>> iSearchUseCase;
    private AutoCloseable closeable;

    @BeforeEach
    void beforeEach() {
        closeable = MockitoAnnotations.openMocks(this);
        iSearchUseCase = new SearchProductUseCase(iProductMapper, iSearchProductRepositoryPort);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void items() {

        //### Given - Objects and Values
        final var id = UUID.randomUUID().toString();
        final var name = "Coca-cola";
        final var description = "description";
        final var image = "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg";
        final var price = 10D * (Math.random() + 1);
        final var now = ZonedDateTime.now(ZoneOffset.UTC);

        final var categoryId = UUID.randomUUID().toString();
        final var categoryDomain = CategoryDomain.builder().id(categoryId).build();

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

        final var productDomainList = List.of(productModel);
        final var productFilterDto = ProductFilterDto.builder().name(name).build();

        //### Given - Mocks
        when(iSearchProductRepositoryPort.findAll(Mockito.any(ProductFilterDto.class))).thenReturn(productDomainList);

        //### When
        final var productDtoList = iSearchUseCase.items(productFilterDto);

        //### Then
        verify(iProductMapper, times(1)).toDto(Mockito.any(ProductModel.class));
        verify(iSearchProductRepositoryPort, times(1)).findAll(Mockito.any(ProductFilterDto.class));

        assertThat(productDtoList)
                .usingRecursiveComparison()
                .isEqualTo(productDomainList);
    }
}