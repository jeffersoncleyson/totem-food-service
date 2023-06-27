package com.totem.food.application.usecases.payment;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.payment.PaymentQRCodeDto;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.web.ISendRequestPort;
import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.domain.exceptions.InvalidStatusException;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.domain.payment.PaymentDomain;
import lombok.SneakyThrows;
import mock.domain.CustomerDomainMock;
import mock.domain.OrderDomainMock;
import mock.domain.PaymentDomainMock;
import mock.ports.in.dto.PaymentCreateDtoMock;
import mock.ports.in.dto.PaymentQRCodeDtoMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePaymentUseCaseTest {

    @Mock
    private ICreateRepositoryPort<PaymentDomain> iCreateRepositoryPort;
    @Mock
    private ISearchUniqueRepositoryPort<Optional<OrderDomain>> iSearchUniqueOrderRepositoryPort;
    @Mock
    private ISearchUniqueRepositoryPort<Optional<CustomerDomain>> iSearchUniqueCustomerRepositoryPort;
    @Mock
    private ISendRequestPort<PaymentDomain, PaymentQRCodeDto> iSendRequest;

    private CreatePaymentUseCase createPaymentUseCase;

    @Mock
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createPaymentUseCase = new CreatePaymentUseCase(iCreateRepositoryPort, iSearchUniqueOrderRepositoryPort, iSearchUniqueCustomerRepositoryPort, iSendRequest);
    }

    @SneakyThrows
    @AfterEach
    void setDown() {
        closeable.close();
    }

    @Test
    void createItemWhenOrderStatusWaitingPayment() {

        //## Mock - Objects
        var orderDomain = OrderDomainMock.getStatusWaitingPaymentMock();
        var customerDomain = CustomerDomainMock.getMock();
        var paymentQRCodeDto = PaymentQRCodeDtoMock.getStatusPendingMock();
        var paymentCreateDto = PaymentCreateDtoMock.getMock();
        var paymentDomain = PaymentDomainMock.getMock();

        //## Give
        when(iSearchUniqueOrderRepositoryPort.findById(anyString())).thenReturn(Optional.ofNullable(orderDomain));
        when(iSearchUniqueCustomerRepositoryPort.findById(anyString())).thenReturn(Optional.of(customerDomain));
        when(iCreateRepositoryPort.saveItem(any(PaymentDomain.class))).thenReturn(paymentDomain);
        when(iSendRequest.sendRequest(any(PaymentDomain.class))).thenReturn(paymentQRCodeDto);

        //## When
        var qrCode = createPaymentUseCase.createItem(paymentCreateDto);

        //## Then
        assertThat(qrCode).usingRecursiveComparison().isEqualTo(paymentQRCodeDto);
        verify(iCreateRepositoryPort, times(1)).saveItem(any());
        verify(iSendRequest, times(1)).sendRequest(any());

    }

    @Test
    void createItemWhenElementNotFoundException() {

        //## Mock - Objects
        var paymentCreateDto = PaymentCreateDtoMock.getMock();

        //## Give
        when(iSearchUniqueOrderRepositoryPort.findById(anyString())).thenReturn(Optional.empty());

        //## When
        var exception = assertThrows(ElementNotFoundException.class,
                () -> createPaymentUseCase.createItem(paymentCreateDto));

        //## Then
        assertEquals(exception.getMessage(), "Order [1] not found");
        verify(iCreateRepositoryPort, never()).saveItem(any());
        verify(iSendRequest, never()).sendRequest(any());
    }

    @Test
    void createItemWhenInvalidStatusException() {

        //## Mock - Objects
        var orderDomain = OrderDomainMock.getStatusNewMock();
        var paymentCreateDto = PaymentCreateDtoMock.getMock();

        //## Give
        when(iSearchUniqueOrderRepositoryPort.findById(anyString())).thenReturn(Optional.ofNullable(orderDomain));

        //## When
        var exception = assertThrows(InvalidStatusException.class,
                () -> createPaymentUseCase.createItem(paymentCreateDto));

        //## Then
        assertEquals(exception.getMessage(), "Invalid Order status [NEW] expected to be [WAITING_PAYMENT]");
        verify(iCreateRepositoryPort, never()).saveItem(any());
        verify(iSendRequest, never()).sendRequest(any());
    }
}