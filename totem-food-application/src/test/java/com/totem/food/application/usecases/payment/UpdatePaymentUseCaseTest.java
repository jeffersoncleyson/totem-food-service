package com.totem.food.application.usecases.payment;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.payment.PaymentFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.domain.payment.PaymentDomain;
import lombok.SneakyThrows;
import mock.domain.OrderDomainMock;
import mock.domain.PaymentDomainMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Closeable;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdatePaymentUseCaseTest {

    @Mock
    private ISearchRepositoryPort<PaymentFilterDto, PaymentDomain> iSearchRepositoryPort;

    @Mock
    private IUpdateRepositoryPort<PaymentDomain> iUpdateRepositoryPort;

    @Mock
    private ISearchUniqueRepositoryPort<Optional<OrderDomain>> iSearchUniqueRepositoryPort;

    @Mock
    private IUpdateRepositoryPort<OrderDomain> iUpdateOrderRepositoryPort;

    private UpdatePaymentUseCase updatePaymentUseCase;

    @Mock
    private Closeable closeable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        updatePaymentUseCase = new UpdatePaymentUseCase(iSearchRepositoryPort, iUpdateRepositoryPort, iSearchUniqueRepositoryPort, iUpdateOrderRepositoryPort);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void updateItemWhenPaymentStatusCompleted() {

        //## Mock - Objects
        var paymentDomain = PaymentDomainMock.getPaymentStatusCompletedMock();
        var paymentFilterDto = PaymentFilterDto.builder().orderId("1").token("token").build();

        //## Given
        when(iSearchRepositoryPort.findAll(any())).thenReturn(paymentDomain);

        //## When
        var updateItem = updatePaymentUseCase.updateItem(paymentFilterDto, anyString());

        //## Then
        assertTrue(updateItem);

    }

    @Test
    void updateItemWhenPaymentStatusPending() {

        //## Mock - Objects
        var paymentDomain = PaymentDomainMock.getPaymentStatusPendingMock();
        var paymentFilterDto = PaymentFilterDto.builder().orderId("1").token("token").build();
        var orderDomain = OrderDomainMock.getStatusWaitingPaymentMock();

        //## Given
        when(iSearchRepositoryPort.findAll(any())).thenReturn(paymentDomain);
        when(iSearchUniqueRepositoryPort.findById(anyString())).thenReturn(Optional.ofNullable(orderDomain));

        //## When
        var updateItem = updatePaymentUseCase.updateItem(paymentFilterDto, anyString());

        //## Then
        assertTrue(updateItem);
        verify(iUpdateOrderRepositoryPort, times(1)).updateItem(orderDomain);
        verify(iUpdateRepositoryPort, times(1)).updateItem(paymentDomain);

    }

    @Test
    void elementNotFoundExceptionWhenSearchUniqueById() {

        //## Mock - Objects
        var paymentDomain = PaymentDomainMock.getPaymentStatusPendingMock();
        var paymentFilterDto = PaymentFilterDto.builder().orderId("1").token("token").build();
        var orderDomain = OrderDomainMock.getStatusWaitingPaymentMock();

        //## Given
        when(iSearchRepositoryPort.findAll(any())).thenReturn(paymentDomain);
        when(iSearchUniqueRepositoryPort.findById(anyString())).thenThrow(new ElementNotFoundException("Order with orderId: 1 not found"));

        //## When
        var exception = assertThrows(ElementNotFoundException.class,
                () -> updatePaymentUseCase.updateItem(paymentFilterDto, anyString()));

        //## Then
        assertEquals(exception.getMessage(), "Order with orderId: 1 not found");
        verify(iUpdateOrderRepositoryPort, never()).updateItem(orderDomain);
        verify(iUpdateRepositoryPort, never()).updateItem(paymentDomain);

    }

    @Test
    void elementNotFoundExceptionWhenPaymentDomainIsNull() {

        //## Mock - Objects
        var paymentFilterDto = PaymentFilterDto.builder().orderId("1").token("token").build();

        //## Given
        when(iSearchRepositoryPort.findAll(any())).thenReturn(null);

        //## When
        var exception = assertThrows(ElementNotFoundException.class,
                () -> updatePaymentUseCase.updateItem(paymentFilterDto, anyString()));

        //## Then
        assertEquals(exception.getMessage(), "Payment with filters orderId: [1] token: [token] not found");
        verify(iUpdateOrderRepositoryPort, never()).updateItem(any());
        verify(iUpdateRepositoryPort, never()).updateItem(any());

    }
}