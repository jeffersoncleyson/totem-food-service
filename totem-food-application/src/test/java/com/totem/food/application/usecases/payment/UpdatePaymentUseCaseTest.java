package com.totem.food.application.usecases.payment;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.payment.PaymentFilterDto;
import com.totem.food.application.ports.in.mappers.payment.IPaymentMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.domain.order.totem.OrderDomain;
import lombok.SneakyThrows;
import mock.domain.OrderDomainMock;
import mock.domain.PaymentDomainMock;
import mock.models.PaymentModelMock;
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

    @Spy
    private IPaymentMapper iPaymentMapper = Mappers.getMapper(IPaymentMapper.class);
    @Mock
    private ISearchRepositoryPort<PaymentFilterDto, PaymentModel> iSearchRepositoryPort;

    @Mock
    private IUpdateRepositoryPort<PaymentModel> iUpdateRepositoryPort;

    @Mock
    private ISearchUniqueRepositoryPort<Optional<OrderDomain>> iSearchUniqueRepositoryPort;

    @Mock
    private IUpdateRepositoryPort<OrderDomain> iUpdateOrderRepositoryPort;

    private UpdatePaymentUseCase updatePaymentUseCase;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        updatePaymentUseCase = new UpdatePaymentUseCase(iPaymentMapper, iSearchRepositoryPort, iUpdateRepositoryPort, iSearchUniqueRepositoryPort, iUpdateOrderRepositoryPort);
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
        var paymentFilterDto = PaymentFilterDto.builder().orderId("1").token("token").build();

        //## Given
        when(iSearchRepositoryPort.findAll(any())).thenReturn(paymentModel);

        //## When
        var updateItem = updatePaymentUseCase.updateItem(paymentFilterDto, anyString());

        //## Then
        assertTrue(updateItem);

    }

    @Test
    void updateItemWhenPaymentStatusPending() {

        //## Mock - Objects
        var paymentModel = PaymentModelMock.getPaymentStatusPendingMock();
        var paymentFilterDto = PaymentFilterDto.builder().orderId("1").token("token").build();
        var orderDomain = OrderDomainMock.getStatusWaitingPaymentMock();

        //## Given
        when(iSearchRepositoryPort.findAll(any())).thenReturn(paymentModel);
        when(iSearchUniqueRepositoryPort.findById(anyString())).thenReturn(Optional.ofNullable(orderDomain));

        //## When
        var updateItem = updatePaymentUseCase.updateItem(paymentFilterDto, anyString());

        //## Then
        assertTrue(updateItem);
        verify(iUpdateOrderRepositoryPort, times(1)).updateItem(orderDomain);
        verify(iUpdateRepositoryPort, times(1)).updateItem(Mockito.any(PaymentModel.class));

    }

    @Test
    void elementNotFoundExceptionWhenSearchUniqueById() {

        //## Mock - Objects
        var paymentModel = PaymentModelMock.getPaymentStatusPendingMock();
        var paymentFilterDto = PaymentFilterDto.builder().orderId("1").token("token").build();
        var orderDomain = OrderDomainMock.getStatusWaitingPaymentMock();

        //## Given
        when(iSearchRepositoryPort.findAll(any())).thenReturn(paymentModel);
        when(iSearchUniqueRepositoryPort.findById(anyString())).thenReturn(Optional.empty());

        //## When
        var exception = assertThrows(ElementNotFoundException.class,
                () -> updatePaymentUseCase.updateItem(paymentFilterDto, anyString()));

        //## Then
        assertEquals(exception.getMessage(), "Order with orderId: [1] not found");
        verify(iUpdateOrderRepositoryPort, never()).updateItem(orderDomain);
        verify(iUpdateRepositoryPort, never()).updateItem(paymentModel);

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