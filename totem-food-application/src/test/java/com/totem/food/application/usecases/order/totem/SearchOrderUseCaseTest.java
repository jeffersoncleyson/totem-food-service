package com.totem.food.application.usecases.order.totem;

import com.totem.food.application.ports.in.dtos.order.totem.OrderFilterDto;
import com.totem.food.application.ports.in.mappers.order.totem.IOrderMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.order.totem.OrderModel;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import lombok.SneakyThrows;
import mock.models.OrderModelMock;
import mock.ports.in.dto.OrderDtoMock;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchOrderUseCaseTest {

    @Spy
    private IOrderMapper iOrderMapper = Mappers.getMapper(IOrderMapper.class);

    @Mock
    private ISearchRepositoryPort<OrderFilterDto, List<OrderModel>> iSearchOrderRepositoryPort;

    private SearchOrderUseCase searchOrderUseCase;

    @Mock
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        searchOrderUseCase = new SearchOrderUseCase(iOrderMapper, iSearchOrderRepositoryPort);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void items() {

        //## Mock - Objects
        var orderFilterDto = OrderFilterDto.builder().orderId("1").customerId("1").build();
        var orderDomain = OrderModelMock.orderModel(OrderStatusEnumDomain.NEW);
        var orderDto = OrderDtoMock.getMock();

        //## Given
        when(iSearchOrderRepositoryPort.findAll(any())).thenReturn(List.of(orderDomain));

        //## When
        var result = searchOrderUseCase.items(orderFilterDto);

        //## Then
        assertNotNull(result);
        assertEquals(result.get(0).getStatus(), orderDto.getStatus());
        verify(iOrderMapper, times(1)).toDto(any(OrderModel.class));

    }
}