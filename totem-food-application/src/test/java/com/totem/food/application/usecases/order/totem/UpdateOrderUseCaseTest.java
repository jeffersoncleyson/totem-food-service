package com.totem.food.application.usecases.order.totem;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.combo.ComboFilterDto;
import com.totem.food.application.ports.in.dtos.order.totem.OrderUpdateDto;
import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
import com.totem.food.application.ports.in.mappers.order.totem.IOrderMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.domain.combo.ComboDomain;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.domain.product.ProductDomain;
import lombok.SneakyThrows;
import mock.domain.OrderDomainMock;
import mock.domain.ProductDomainMock;
import mock.ports.in.dto.OrderUpdateDtoMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Closeable;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateOrderUseCaseTest {

    @Spy
    private IOrderMapper iOrderMapper = Mappers.getMapper(IOrderMapper.class);

    @Mock
    private ISearchUniqueRepositoryPort<Optional<OrderDomain>> iSearchUniqueRepositoryPort;

    @Mock
    private ISearchRepositoryPort<ComboFilterDto, List<ComboDomain>> iSearchComboDomainRepositoryPort;

    @Mock
    private ISearchRepositoryPort<ProductFilterDto, List<ProductDomain>> iSearchProductRepositoryPort;

    @Mock
    private IUpdateRepositoryPort<OrderDomain> iProductRepositoryPort;

    private UpdateOrderUseCase updateOrderUseCase;

    @Mock
    private Closeable closeable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        updateOrderUseCase = new UpdateOrderUseCase(iOrderMapper, iSearchUniqueRepositoryPort, iSearchComboDomainRepositoryPort, iSearchProductRepositoryPort, iProductRepositoryPort);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }


    @Test
    void updateItemWhenOrderIdDoesNotExistThenThrowException() {

        //## Given
        when(iSearchUniqueRepositoryPort.findById(anyString())).thenReturn(Optional.empty());

        //## When
        var exception = assertThrows(ElementNotFoundException.class,
                () -> updateOrderUseCase.updateItem(new OrderUpdateDto(), anyString()));

        //## Then
        Assertions.assertEquals(exception.getMessage(), "Order [] not found");

    }

    @Test
    void updateItem() {

        //## Mock - Object
        var orderUpdateDto = OrderUpdateDtoMock.getMock();
        var orderDomainOpt = OrderDomainMock.getStatusNewMock();
        var productFilterDto = ProductFilterDto.builder().build();
        var productDomain = ProductDomainMock.getMock();
        productDomain.setId("7518d878-6cd4-4d74-bf76-c21345f2f7da");


        //## Given
        when(iSearchUniqueRepositoryPort.findById(anyString()))
                .thenReturn(Optional.of(orderDomainOpt));
        when(iSearchProductRepositoryPort.findAll(any(ProductFilterDto.class)))
                .thenReturn(List.of(productDomain));


        //## When
        var orderDto = updateOrderUseCase.updateItem(orderUpdateDto, "1");

        //## Then
        Assertions.assertNotNull(orderDto);

    }
}