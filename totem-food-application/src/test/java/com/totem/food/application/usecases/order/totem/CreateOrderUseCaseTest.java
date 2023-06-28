package com.totem.food.application.usecases.order.totem;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.exceptions.InvalidInput;
import com.totem.food.application.ports.in.dtos.combo.ComboFilterDto;
import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
import com.totem.food.application.ports.in.mappers.order.totem.IOrderMapper;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.domain.combo.ComboDomain;
import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.domain.product.ProductDomain;
import lombok.SneakyThrows;
import mock.domain.ComboDomainMock;
import mock.domain.CustomerDomainMock;
import mock.domain.OrderDomainMock;
import mock.domain.ProductDomainMock;
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
    @Mock
    private ICreateRepositoryPort<OrderDomain> iCreateRepositoryPort;
    @Mock
    private ISearchUniqueRepositoryPort<Optional<CustomerDomain>> iSearchUniqueCustomerRepositoryPort;
    @Mock
    private ISearchRepositoryPort<ProductFilterDto, List<ProductDomain>> iSearchProductRepositoryPort;
    @Mock
    private ISearchRepositoryPort<ComboFilterDto, List<ComboDomain>> iSearchDomainRepositoryPort;

    private CreateOrderUseCase createOrderUseCase;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        createOrderUseCase = new CreateOrderUseCase(iOrderMapper, iCreateRepositoryPort, iSearchUniqueCustomerRepositoryPort, iSearchProductRepositoryPort, iSearchDomainRepositoryPort);
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
        var customerDomain = CustomerDomainMock.getMock();
        var productDomain = ProductDomainMock.getMock();

        //## Given
        when(iSearchUniqueCustomerRepositoryPort.findById(anyString())).thenReturn(Optional.of(customerDomain));
        when(iSearchProductRepositoryPort.findAll(any(ProductFilterDto.class))).thenReturn(List.of(productDomain));

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
        var customerDomain = CustomerDomainMock.getMock();
        var productDomain = ProductDomainMock.getMock();
        var comboDomain = ComboDomainMock.getMock();
        var orderCreateDto = OrderCreateDtoMock.getMock(productDomain.getId(), comboDomain.getId());

        //## Given
        when(iCreateRepositoryPort.saveItem(any(OrderDomain.class))).thenReturn(orderDomain);
        when(iSearchUniqueCustomerRepositoryPort.findById(anyString())).thenReturn(Optional.of(customerDomain));
        when(iSearchProductRepositoryPort.findAll(any(ProductFilterDto.class))).thenReturn(List.of(productDomain));
        when(iSearchDomainRepositoryPort.findAll(any(ComboFilterDto.class))).thenReturn(List.of(comboDomain));

        //## When
        var orderDto = createOrderUseCase.createItem(orderCreateDto);

        //## Then
        assertThat(orderDto).usingRecursiveComparison().isNotNull();
        verify(iOrderMapper, times(1)).toDto(orderDomain);
    }
}