package com.totem.food.application.usecases.payment;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.payment.PaymentQRCodeDto;
import com.totem.food.application.ports.in.mappers.customer.ICustomerMapper;
import com.totem.food.application.ports.in.mappers.order.totem.IOrderMapper;
import com.totem.food.application.ports.in.mappers.payment.IPaymentMapper;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.application.ports.out.persistence.order.totem.OrderModel;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.application.ports.out.web.ISendRequestPort;
import com.totem.food.domain.exceptions.InvalidStatusException;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import lombok.SneakyThrows;
import mock.models.CustomerModelMock;
import mock.models.OrderModelMock;
import mock.models.PaymentModelMock;
import mock.ports.in.dto.PaymentCreateDtoMock;
import mock.ports.in.dto.PaymentQRCodeDtoMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
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
    private ICreateRepositoryPort<PaymentModel> iCreateRepositoryPort;
    @Mock
    private IUpdateRepositoryPort<PaymentModel> iUpdateRepositoryPort;
    @Spy
    private IOrderMapper iOrderMapper = Mappers.getMapper(IOrderMapper.class);
    @Spy
    private ICustomerMapper iCustomerMapper = Mappers.getMapper(ICustomerMapper.class);
    @Spy
    private IPaymentMapper iPaymentMapper = Mappers.getMapper(IPaymentMapper.class);
    @Mock
    private ISearchUniqueRepositoryPort<Optional<OrderModel>> iSearchUniqueOrderRepositoryPort;
    @Mock
    private ISearchUniqueRepositoryPort<Optional<CustomerModel>> iSearchUniqueCustomerRepositoryPort;
    @Mock
    private ISendRequestPort<PaymentModel, PaymentQRCodeDto> iSendRequest;

    private CreatePaymentUseCase createPaymentUseCase;

    @Mock
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createPaymentUseCase = new CreatePaymentUseCase(iCreateRepositoryPort, iUpdateRepositoryPort, iOrderMapper, iCustomerMapper, iPaymentMapper, iSearchUniqueOrderRepositoryPort, iSearchUniqueCustomerRepositoryPort, iSendRequest);
    }

    @SneakyThrows
    @AfterEach
    void setDown() {
        closeable.close();
    }

    @Test
    void createItemWhenOrderStatusWaitingPayment() {

        //## Mock - Objects
        var orderDomain = OrderModelMock.orderModel(OrderStatusEnumDomain.WAITING_PAYMENT);
        var customerModel = CustomerModelMock.getMock();
        var paymentQRCodeDto = PaymentQRCodeDtoMock.getStatusPendingMock();
        var paymentCreateDto = PaymentCreateDtoMock.getMock();
        var paymentModel = PaymentModelMock.getPaymentStatusPendingMock();

        //## Give
        when(iSearchUniqueOrderRepositoryPort.findById(anyString())).thenReturn(Optional.ofNullable(orderDomain));
        when(iSearchUniqueCustomerRepositoryPort.findById(anyString())).thenReturn(Optional.of(customerModel));
        when(iCreateRepositoryPort.saveItem(any(PaymentModel.class))).thenReturn(paymentModel);
        when(iSendRequest.sendRequest(any(PaymentModel.class))).thenReturn(paymentQRCodeDto);

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
        var orderDomain = OrderModelMock.orderModel(OrderStatusEnumDomain.NEW);
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