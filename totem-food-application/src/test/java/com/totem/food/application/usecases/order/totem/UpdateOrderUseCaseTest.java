package com.totem.food.application.usecases.order.totem;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.order.totem.OrderUpdateDto;
import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
import com.totem.food.application.ports.in.mappers.order.totem.IOrderMapper;
import com.totem.food.application.ports.in.mappers.product.IProductMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.ports.out.persistence.order.totem.OrderModel;
import com.totem.food.application.ports.out.persistence.product.ProductModel;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import lombok.SneakyThrows;
import mock.domain.ProductDomainMock;
import mock.models.OrderModelMock;
import mock.models.ProductModelMock;
import mock.ports.in.dto.OrderUpdateDtoMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateOrderUseCaseTest {

    @Spy
    private IOrderMapper iOrderMapper = Mappers.getMapper(IOrderMapper.class);

    @Spy
    private IProductMapper iProductMapper = Mappers.getMapper(IProductMapper.class);

    @Mock
    private ISearchUniqueRepositoryPort<Optional<OrderModel>> iSearchUniqueRepositoryPort;

    @Mock
    private ISearchRepositoryPort<ProductFilterDto, List<ProductModel>> iSearchProductRepositoryPort;

    @Mock
    private IUpdateRepositoryPort<OrderModel> iProductRepositoryPort;

    private UpdateOrderUseCase updateOrderUseCase;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        updateOrderUseCase = new UpdateOrderUseCase(iOrderMapper, iProductMapper, iSearchUniqueRepositoryPort, iSearchProductRepositoryPort, iProductRepositoryPort);
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
        assertEquals(exception.getMessage(), "Order [] not found");

    }

    @Test
    void updateItem() {

        //## Mock - Object
        var productDomain = ProductDomainMock.getMock();
        var productModel = ProductModelMock.getMock();
        productModel.setId(productDomain.getId());

        var orderUpdateDto = OrderUpdateDtoMock.getMock(productDomain.getId());

        var orderDomainOpt = OrderModelMock.orderModel(OrderStatusEnumDomain.NEW);
        orderDomainOpt.setProducts(List.of(productDomain));

        //## Given
        when(iSearchUniqueRepositoryPort.findById(anyString()))
                .thenReturn(Optional.of(orderDomainOpt));
        when(iSearchProductRepositoryPort.findAll(any(ProductFilterDto.class)))
                .thenReturn(List.of(productModel));

        when(iProductRepositoryPort.updateItem(any(OrderModel.class))).thenReturn(orderDomainOpt);

        //## When
        var orderDto = updateOrderUseCase.updateItem(orderUpdateDto, anyString());

        //## Then
        assertThat(orderDto).usingRecursiveComparison().isNotNull();
        verify(iOrderMapper, times(1)).toDto(any(OrderModel.class));

    }
}