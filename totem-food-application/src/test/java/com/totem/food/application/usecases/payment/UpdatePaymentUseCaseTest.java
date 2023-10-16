package com.totem.food.application.usecases.payment;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.payment.PaymentElementDto;
import com.totem.food.application.ports.in.dtos.payment.PaymentFilterDto;
import com.totem.food.application.ports.in.mappers.order.totem.IOrderMapper;
import com.totem.food.application.ports.in.mappers.payment.IPaymentMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.ports.out.persistence.order.totem.OrderModel;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.application.ports.out.web.ISendRequestPort;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import lombok.SneakyThrows;
import mock.models.OrderModelMock;
import mock.models.PaymentModelMock;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdatePaymentUseCaseTest {

    @Spy
    private IPaymentMapper iPaymentMapper = Mappers.getMapper(IPaymentMapper.class);
    @Spy
    private IOrderMapper iOrderMapper = Mappers.getMapper(IOrderMapper.class);
    @Mock
    private ISearchRepositoryPort<PaymentFilterDto, List<PaymentModel>> iSearchRepositoryPort;

    @Mock
    private IUpdateRepositoryPort<PaymentModel> iUpdateRepositoryPort;

    @Mock
    private ISearchUniqueRepositoryPort<Optional<OrderModel>> iSearchOrderModel;

    @Mock
    private IUpdateRepositoryPort<OrderModel> iUpdateOrderRepositoryPort;

    @Mock
    private ISendRequestPort<String, PaymentElementDto> iSendRequest;

    private UpdatePaymentUseCase updatePaymentUseCase;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        updatePaymentUseCase = new UpdatePaymentUseCase(
                iPaymentMapper,
                iOrderMapper,
                iUpdateRepositoryPort,
                iSearchOrderModel,
                iUpdateOrderRepositoryPort,
                iSearchRepositoryPort,
                iSendRequest);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void updateItemWhenPaymentStatusCompleted() {

        //## Mock - Objects
        var paymentModel = PaymentModelMock.getPaymentStatusCompletedMock();

        //## Given
        when(iSearchRepositoryPort.findAll(any())).thenReturn(List.of(paymentModel));

        //## When
        var updateItem = updatePaymentUseCase.updateItem(any(), anyString());

        //## Then
        assertTrue(updateItem);

    }

    @Test
    void updateItemWhenPaymentStatusPending() {

        //## Mock - Objects
        var paymentModel = PaymentModelMock.getPaymentStatusPendingMock();
        var orderDomain = OrderModelMock.orderModel(OrderStatusEnumDomain.WAITING_PAYMENT);

        //## Given
        when(iSearchRepositoryPort.findAll(any())).thenReturn(List.of(paymentModel));
        when(iSearchOrderModel.findById(anyString())).thenReturn(Optional.ofNullable(orderDomain));

        //## When
        var updateItem = updatePaymentUseCase.updateItem(any(), anyString());

        //## Then
        assertTrue(updateItem);
        verify(iUpdateOrderRepositoryPort, times(1)).updateItem(any(OrderModel.class));
        verify(iUpdateRepositoryPort, times(1)).updateItem(any(PaymentModel.class));

    }

    @Test
    void elementNotFoundExceptionWhenSearchUniqueById() {

        //## Mock - Objects
        var paymentModel = PaymentModelMock.getPaymentStatusPendingMock();
        var orderDomain = OrderModelMock.orderModel(OrderStatusEnumDomain.WAITING_PAYMENT);

        //## Given
        when(iSearchRepositoryPort.findAll(any())).thenReturn(List.of(paymentModel));
        when(iSearchOrderModel.findById(anyString())).thenReturn(Optional.empty());

        //## When
        var exception = assertThrows(ElementNotFoundException.class,
                () -> updatePaymentUseCase.updateItem(any(), anyString()));

        //## Then
        assertEquals(exception.getMessage(), "Order with orderId: [1] not found");
        verify(iUpdateOrderRepositoryPort, never()).updateItem(orderDomain);
        verify(iUpdateRepositoryPort, never()).updateItem(paymentModel);

    }

    @Test
    void elementNotFoundExceptionWhenPaymentDomainIsNull() {
        
        //## Given
        when(iSearchRepositoryPort.findAll(any())).thenReturn(null);

        //## When
        var exception = assertThrows(ElementNotFoundException.class,
                () -> updatePaymentUseCase.updateItem(any(), anyString()));

        //## Then
        assertEquals(exception.getMessage(), "Payment with filters orderId: [1] token: [token] not found");
        verify(iUpdateOrderRepositoryPort, never()).updateItem(any());
        verify(iUpdateRepositoryPort, never()).updateItem(any());

    }
}