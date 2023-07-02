package com.totem.food.application.usecases.order.totem;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.exceptions.InvalidInput;
import com.totem.food.application.ports.in.dtos.combo.ComboFilterDto;
import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
import com.totem.food.application.ports.in.mappers.customer.ICustomerMapper;
import com.totem.food.application.ports.in.mappers.order.totem.IOrderMapper;
import com.totem.food.application.ports.in.mappers.product.IProductMapper;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.application.ports.out.persistence.product.ProductModel;
import com.totem.food.domain.combo.ComboDomain;
import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.domain.product.ProductDomain;
import lombok.SneakyThrows;
import mock.domain.ComboDomainMock;
import mock.domain.CustomerDomainMock;
import mock.domain.OrderDomainMock;
import mock.domain.ProductDomainMock;
import mock.models.CustomerModelMock;
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
    private ICreateRepositoryPort<OrderDomain> iCreateRepositoryPort;
    @Mock
    private ISearchUniqueRepositoryPort<Optional<CustomerModel>> iSearchUniqueCustomerRepositoryPort;
    @Mock
    private ISearchRepositoryPort<ProductFilterDto, List<ProductModel>> iSearchProductRepositoryPort;
    @Mock
    private ISearchRepositoryPort<ComboFilterDto, List<ComboDomain>> iSearchDomainRepositoryPort;

    private CreateOrderUseCase createOrderUseCase;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        createOrderUseCase = new CreateOrderUseCase(iOrderMapper, iCustomerMapper, iProductMapper, iCreateRepositoryPort, iSearchUniqueCustomerRepositoryPort, iSearchProductRepositoryPort, iSearchDomainRepositoryPort);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void invalidInputWhenCreateItem() {

        //## Given - Mock - Objects
        var orderCreateDto = OrderCreateDtoMock.getMock("", "");
        orderCreateDto.setProducts(List.of());
        orderCreateDto.setCombos(List.of());

        //## When
        var exception = assertThrows(InvalidInput.class,
                () -> createOrderUseCase.createItem(orderCreateDto));

        //## Then
        assertEquals(exception.getMessage(), "Order is invalid");

    }

    @Test
    void elementNotFoundExceptionWhenProductToDomain() {
        //## Mock - Objects
        var orderCreateDto = OrderCreateDtoMock.getMock("", "");
        var customerModel = CustomerModelMock.getMock();
        var productModel = ProductModelMock.getMock();

        //## Given
        when(iSearchUniqueCustomerRepositoryPort.findById(anyString())).thenReturn(Optional.of(customerModel));
        when(iSearchProductRepositoryPort.findAll(any(ProductFilterDto.class))).thenReturn(List.of(productModel));

        //## When
        var exception = assertThrows(ElementNotFoundException.class,
                () -> createOrderUseCase.createItem(orderCreateDto)
        );

        //## Then
        assertEquals(exception.getMessage(), "Combos [[]] some combos are invalid");
        verify(iOrderMapper, never()).toDto(any());
    }

    @Test
    void createItem() {
        //## Mock - Objects

        var orderDomain = OrderDomainMock.getStatusNewMock();
        var customerModel = CustomerModelMock.getMock();
        var productModel = ProductModelMock.getMock();
        var comboDomain = ComboDomainMock.getMock();
        var orderCreateDto = OrderCreateDtoMock.getMock(productModel.getId(), comboDomain.getId());

        //## Given
        when(iCreateRepositoryPort.saveItem(any(OrderDomain.class))).thenReturn(orderDomain);
        when(iSearchUniqueCustomerRepositoryPort.findById(anyString())).thenReturn(Optional.of(customerModel));
        when(iSearchProductRepositoryPort.findAll(any(ProductFilterDto.class))).thenReturn(List.of(productModel));
        when(iSearchDomainRepositoryPort.findAll(any(ComboFilterDto.class))).thenReturn(List.of(comboDomain));

        //## When
        var orderDto = createOrderUseCase.createItem(orderCreateDto);

        //## Then
        assertThat(orderDto).usingRecursiveComparison().isNotNull();
        verify(iOrderMapper, times(1)).toDto(orderDomain);
    }
}