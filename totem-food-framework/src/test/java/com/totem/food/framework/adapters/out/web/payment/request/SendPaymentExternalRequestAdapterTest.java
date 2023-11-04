package com.totem.food.framework.adapters.out.web.payment.request;

import com.totem.food.application.ports.in.dtos.payment.PaymentQRCodeDto;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.application.ports.out.web.ISendRequestPort;
import com.totem.food.domain.payment.PaymentDomain;
import com.totem.food.framework.adapters.out.web.payment.client.MercadoPagoClient;
import com.totem.food.framework.adapters.out.web.payment.mapper.IPaymentResponseMapper;
import lombok.SneakyThrows;
import mocks.adapters.out.web.payment.request.PaymentResponseEntityMock;
import mocks.models.PaymentModelMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SendPaymentExternalRequestAdapterTest {

    @Spy
    private IPaymentResponseMapper iPaymentResponseMapper = Mappers.getMapper(IPaymentResponseMapper.class);

    @Spy
    private MercadoPagoClient mercadoPagoClient;

    private ISendRequestPort<PaymentModel, PaymentQRCodeDto> iSendRequestPort;

    private AutoCloseable closeable;


    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        iSendRequestPort = new SendPaymentExternalRequestAdapter(iPaymentResponseMapper, mercadoPagoClient);
        ReflectionTestUtils.setField(iSendRequestPort, "posId", "posId123");
        ReflectionTestUtils.setField(iSendRequestPort, "userId", "userId123");
        ReflectionTestUtils.setField(iSendRequestPort, "token", "tokenAuthorization");
        ReflectionTestUtils.setField(iSendRequestPort, "paymentCallback", "paymentCallbackURL");
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void testSendRequest() {

        //## Mock - Objects
        final var paymentModel = PaymentModelMock.getPaymentDomain(PaymentDomain.PaymentStatus.PENDING);
        final var paymentResponseEntity = PaymentResponseEntityMock.responseEntity();
        paymentModel.setQrcodeBase64("qrcodeBase64");

        //## Given
        given(mercadoPagoClient.createOrder(any(), any(), any(), any()))
                .willReturn(ResponseEntity.ok(paymentResponseEntity));

        //## When
        PaymentQRCodeDto result = iSendRequestPort.sendRequest(paymentModel);

        //## Then
        assertThat(result).usingRecursiveAssertion().isNotNull();
        verify(iPaymentResponseMapper, times(1)).toDto(paymentResponseEntity);

    }

}