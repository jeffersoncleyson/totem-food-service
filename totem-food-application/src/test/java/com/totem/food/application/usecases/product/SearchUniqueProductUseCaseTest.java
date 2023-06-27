package com.totem.food.application.usecases.product;

import com.totem.food.application.ports.in.mappers.product.IProductMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.domain.product.ProductDomain;
import mock.domain.ProductDomainMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchUniqueProductUseCaseTest {

    @Spy
    private IProductMapper iProductEntityMapper = Mappers.getMapper(IProductMapper.class);

    @Mock
    private ISearchUniqueRepositoryPort<Optional<ProductDomain>> iSearchUniqueRepositoryPort;

    private SearchUniqueProductUseCase searchUniqueProductUseCase;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.searchUniqueProductUseCase = new SearchUniqueProductUseCase(iProductEntityMapper, iSearchUniqueRepositoryPort);
    }

    @Test
    void item() {

        //## Given
        final var productDomain =  ProductDomainMock.getMock();
        when(iSearchUniqueRepositoryPort.findById(anyString())).thenReturn(Optional.of(productDomain));

        //## When
        final var productDto = searchUniqueProductUseCase.item(anyString());

        //## Then
        assertNotNull(productDto);
        verify(iProductEntityMapper, times(1)).toDto(any(ProductDomain.class));
    }
}