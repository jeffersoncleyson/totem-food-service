package com.totem.food.application.usecases.order.totem;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.order.totem.OrderDto;
import com.totem.food.application.ports.in.dtos.payment.PaymentFilterDto;
import com.totem.food.application.ports.in.mappers.order.totem.IOrderMapper;
import com.totem.food.application.ports.out.dtos.EmailNotificationDto;
import com.totem.food.application.ports.out.email.ISendEmailPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.usecases.commons.IUpdateStatusUseCase;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.domain.payment.PaymentDomain;
import lombok.SneakyThrows;
import mock.domain.OrderDomainMock;
import mock.domain.PaymentDomainMock;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateStatusOrderUseCaseTest {

    @Spy
    private IOrderMapper iOrderMapper = Mappers.getMapper(IOrderMapper.class);
    @Mock
    private ISearchUniqueRepositoryPort<Optional<OrderDomain>> iSearchUniqueRepositoryPort;
    @Mock
    private IUpdateRepositoryPort<OrderDomain> iProductRepositoryPort;
    @Mock
    private ISendEmailPort<EmailNotificationDto, Boolean> iSendEmailPort;
    @Mock
    private ISearchRepositoryPort<PaymentFilterDto, PaymentDomain> iSearchPaymentRepositoryPort;

    private IUpdateStatusUseCase<OrderDto> iUpdateStatusUseCase;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        iUpdateStatusUseCase = new UpdateStatusOrderUseCase(
            iOrderMapper,
            iSearchUniqueRepositoryPort,
            iProductRepositoryPort,
            iSendEmailPort,
            iSearchPaymentRepositoryPort
        );
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void updateStatus() {

        //## Mock - Objects
        var orderDomain = OrderDomainMock.getOrderDomain(OrderStatusEnumDomain.NEW);

        //## Mocks
        when(iSearchUniqueRepositoryPort.findById(Mockito.anyString())).thenReturn(Optional.of(orderDomain));

        //## When
        final var orderDto = iUpdateStatusUseCase.updateStatus(orderDomain.getId(), orderDomain.getStatus().key);

        //## Then
        verify(iOrderMapper, times(1)).toDto(any());
        verify(iSearchUniqueRepositoryPort, times(1)).findById(Mockito.anyString());

        final var orderDtoExpected = iOrderMapper.toDto(orderDomain);
        assertThat(orderDto).usingRecursiveComparison()
                .isEqualTo(orderDtoExpected);

    }

    @Test
    void updateStatusOrderNotFound() {

        //## Mock - Objects
        var orderDomain = OrderDomainMock.getOrderDomain(OrderStatusEnumDomain.NEW);

        //## Mocks
        when(iSearchUniqueRepositoryPort.findById(Mockito.anyString())).thenReturn(Optional.empty());

        //## When
        final var exceptions = assertThrows(
                ElementNotFoundException.class,
                () -> iUpdateStatusUseCase.updateStatus(orderDomain.getId(), orderDomain.getStatus().key)
        );

        //## Then
        verify(iSearchUniqueRepositoryPort, times(1)).findById(Mockito.anyString());
        assertEquals(String.format("Order [%s] not found", orderDomain.getId()), exceptions.getMessage());
    }

    @Test
    void updateStatusChangeStatusToReceived() {

        //## Mock - Objects
        var orderDomain = OrderDomainMock.getOrderDomain(OrderStatusEnumDomain.WAITING_PAYMENT);
        final var paymentDomain = PaymentDomainMock.getPaymentStatusCompletedMock();

        //## Mocks
        when(iSearchUniqueRepositoryPort.findById(Mockito.anyString())).thenReturn(Optional.of(orderDomain));
        when(iSearchPaymentRepositoryPort.findAll(Mockito.any(PaymentFilterDto.class))).thenReturn(paymentDomain);
        when(iProductRepositoryPort.updateItem(Mockito.any(OrderDomain.class))).thenReturn(orderDomain);

        //## When
        final var orderDto = iUpdateStatusUseCase.updateStatus(orderDomain.getId(), OrderStatusEnumDomain.RECEIVED.key);

        //## Then
        verify(iOrderMapper, times(1)).toDto(any());
        verify(iSearchUniqueRepositoryPort, times(1)).findById(Mockito.anyString());
        verify(iSearchPaymentRepositoryPort, times(1)).findAll(Mockito.any(PaymentFilterDto.class));
        verify(iProductRepositoryPort, times(1)).updateItem(Mockito.any(OrderDomain.class));

        final var orderDtoExpected = iOrderMapper.toDto(orderDomain);
        assertThat(orderDto).usingRecursiveComparison()
                .isEqualTo(orderDtoExpected);

    }

    @Test
    void updateStatusChangeStatusToReceivedButPaymentStatusPending() {

        //## Mock - Objects
        var orderDomain = OrderDomainMock.getOrderDomain(OrderStatusEnumDomain.WAITING_PAYMENT);

        //## Mocks
        when(iSearchUniqueRepositoryPort.findById(Mockito.anyString())).thenReturn(Optional.of(orderDomain));
        when(iSearchPaymentRepositoryPort.findAll(Mockito.any(PaymentFilterDto.class))).thenReturn(null);

        //## When
        final var exceptions = assertThrows(
                ElementNotFoundException.class,
                () -> iUpdateStatusUseCase.updateStatus(orderDomain.getId(), OrderStatusEnumDomain.RECEIVED.key)
        );

        //## Then
        verify(iSearchUniqueRepositoryPort, times(1)).findById(Mockito.anyString());
        verify(iSearchPaymentRepositoryPort, times(1)).findAll(Mockito.any(PaymentFilterDto.class));
        assertEquals(
                String.format("Order [%s] needs a payment request or Payment is PENDING",
                orderDomain.getId()), exceptions.getMessage()
        );
    }

    @Test
    void updateStatusChangeStatusToReady() {

        //## Mock - Objects
        var orderDomain = OrderDomainMock.getOrderDomain(OrderStatusEnumDomain.IN_PREPARATION);
        var orderDomainReady = OrderDomainMock.getOrderDomain(OrderStatusEnumDomain.READY);

        //## Mocks
        when(iSearchUniqueRepositoryPort.findById(Mockito.anyString())).thenReturn(Optional.of(orderDomain));
        when(iProductRepositoryPort.updateItem(Mockito.any(OrderDomain.class))).thenReturn(orderDomainReady);

        //## When
        final var orderDto = iUpdateStatusUseCase.updateStatus(orderDomain.getId(), OrderStatusEnumDomain.READY.key);

        //## Then
        verify(iOrderMapper, times(1)).toDto(any());
        verify(iSearchUniqueRepositoryPort, times(1)).findById(Mockito.anyString());
        verify(iSearchPaymentRepositoryPort, times(0)).findAll(Mockito.any(PaymentFilterDto.class));
        verify(iProductRepositoryPort, times(1)).updateItem(Mockito.any(OrderDomain.class));
        verify(iSendEmailPort, times(1)).sendEmail(Mockito.any(EmailNotificationDto.class));

        final var orderDtoExpected = iOrderMapper.toDto(orderDomainReady);
        assertThat(orderDto).usingRecursiveComparison()
                .isEqualTo(orderDtoExpected);

    }
}