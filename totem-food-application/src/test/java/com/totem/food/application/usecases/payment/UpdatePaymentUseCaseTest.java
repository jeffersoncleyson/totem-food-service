package com.totem.food.application.usecases.payment;

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

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.totem.food.domain.payment.PaymentDomain.PaymentStatus.PENDING;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    private IUpdateRepositoryPort<PaymentModel> iUpdateRepositoryPort;

    @Mock
    private ISearchUniqueRepositoryPort<Optional<OrderModel>> iSearchOrderModel;

    @Mock
    private IUpdateRepositoryPort<OrderModel> iUpdateOrderRepositoryPort;

    @Mock
    private ISearchRepositoryPort<PaymentFilterDto, List<PaymentModel>> iSearchRepositoryPort;

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
    void testUpdateWhenListEmpty() {

        //## Mock - Objects
        var filter = PaymentFilterDto.builder()
                .status(PENDING.name())
                .timeLastOrders(ZonedDateTime.now().minusMinutes(30))
                .build();

        //## Given
        when(iSearchRepositoryPort.findAll(any())).thenReturn(List.of());

        //## When
        var result = updatePaymentUseCase.updateItem(filter, UUID.randomUUID().toString());

        //## Then
        assertFalse(result);
    }

    @Test
    void testeUpdateItemWhenSuccessUpdateOrderWithPayment() {

        //## Mock - Objects
        String id = UUID.randomUUID().toString();
        var filter = PaymentFilterDto.builder()
                .status(PENDING.name())
                .timeLastOrders(ZonedDateTime.now().minusMinutes(30))
                .build();

        var paymentsModel = List.of(PaymentModelMock.getPaymentStatusPendingMock());

        var paymentElementDto = PaymentElementDto.builder()
                .orderStatus("paid")
                .build();

        var orderModel = OrderModelMock.orderModel(OrderStatusEnumDomain.WAITING_PAYMENT);

        //## When
        when(iSearchRepositoryPort.findAll(any())).thenReturn(paymentsModel);
        when(iSendRequest.sendRequest(anyString())).thenReturn(paymentElementDto);
        when(iSearchOrderModel.findById(anyString())).thenReturn(Optional.of(orderModel));

        //## Given
        var result = updatePaymentUseCase.updateItem(filter, id);

        //## Then
        assertTrue(result);
        verify(iUpdateOrderRepositoryPort, times(1)).updateItem(any(OrderModel.class));
        verify(iUpdateRepositoryPort, times(1)).updateItem(any(PaymentModel.class));
    }

}
