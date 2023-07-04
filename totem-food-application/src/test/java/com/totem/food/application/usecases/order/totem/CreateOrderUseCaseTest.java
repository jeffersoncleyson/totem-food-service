package com.totem.food.application.usecases.order.totem;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.exceptions.InvalidInput;
import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
import com.totem.food.application.ports.in.mappers.customer.ICustomerMapper;
import com.totem.food.application.ports.in.mappers.order.totem.IOrderMapper;
import com.totem.food.application.ports.in.mappers.product.IProductMapper;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.application.ports.out.persistence.order.totem.OrderModel;
import com.totem.food.application.ports.out.persistence.product.ProductModel;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import lombok.SneakyThrows;
import mock.models.CustomerModelMock;
import mock.models.OrderModelMock;
import mock.models.ProductModelMock;
import mock.ports.in.dto.OrderCreateDtoMock;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

    @Spy
    private IOrderMapper iOrderMapper = Mappers.getMapper(IOrderMapper.class);
    @Spy
    private ICustomerMapper iCustomerMapper = Mappers.getMapper(ICustomerMapper.class);
    @Spy
    private IProductMapper iProductMapper = Mappers.getMapper(IProductMapper.class);
    @Mock
    private ICreateRepositoryPort<OrderModel> iCreateRepositoryPort;
    @Mock
    private ISearchUniqueRepositoryPort<Optional<CustomerModel>> iSearchUniqueCustomerRepositoryPort;
    @Mock
    private ISearchRepositoryPort<ProductFilterDto, List<ProductModel>> iSearchProductRepositoryPort;

    private CreateOrderUseCase createOrderUseCase;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        createOrderUseCase = new CreateOrderUseCase(iOrderMapper, iCustomerMapper, iProductMapper, iCreateRepositoryPort, iSearchUniqueCustomerRepositoryPort, iSearchProductRepositoryPort);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void invalidInputWhenCreateItem() {

        //## Given - Mock - Objects
        var orderCreateDto = OrderCreateDtoMock.getMock("");
        orderCreateDto.setProducts(List.of());

        //## When
        var exception = assertThrows(InvalidInput.class,
                () -> createOrderUseCase.createItem(orderCreateDto));

        //## Then
        assertEquals(exception.getMessage(), "Order is invalid");

    }

    @Test
    void createItem() {
        //## Mock - Objects

        var orderDomain = OrderModelMock.orderModel(OrderStatusEnumDomain.NEW);
        var customerModel = CustomerModelMock.getMock();
        var productModel = ProductModelMock.getMock();
        var orderCreateDto = OrderCreateDtoMock.getMock(productModel.getId());

        //## Given
        when(iCreateRepositoryPort.saveItem(any(OrderModel.class))).thenReturn(orderDomain);
        when(iSearchUniqueCustomerRepositoryPort.findById(anyString())).thenReturn(Optional.of(customerModel));
        when(iSearchProductRepositoryPort.findAll(any(ProductFilterDto.class))).thenReturn(List.of(productModel));

        //## When
        var orderDto = createOrderUseCase.createItem(orderCreateDto);

        //## Then
        assertThat(orderDto).usingRecursiveComparison().isNotNull();
        verify(iOrderMapper, times(1)).toDto(orderDomain);
    }
}