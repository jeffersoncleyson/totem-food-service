package com.totem.food.framework.adapters.out.web.payment.request;

import com.totem.food.application.ports.in.dtos.payment.PaymentElementDto;
import com.totem.food.application.ports.out.web.ISendRequestPort;
import com.totem.food.framework.adapters.out.web.payment.client.MercadoPagoClient;
import lombok.SneakyThrows;
import mocks.entity.ElementResponseEntityMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SearchPaymentExternalRequestAdapterTest {

    private ISendRequestPort<String, PaymentElementDto> iSendRequestPort;

    @Spy
    private MercadoPagoClient mercadoPagoClient;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        iSendRequestPort = new SearchPaymentExternalRequestAdapter(mercadoPagoClient);
        ReflectionTestUtils.setField(iSendRequestPort, "token", "tokenAuthorization");
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void sendRequest() {

        //## Mocks - Objects
        String externalPaymentId = "12345";
        var elementResponseEntity = ElementResponseEntityMock.getElementResponseEntity();
        var paymentElementDto = new PaymentElementDto();
        paymentElementDto.setExternalPaymentId(externalPaymentId);

        //## Given
        given(mercadoPagoClient.getOrderDetails(any(), any()))
                .willReturn(ResponseEntity.ok(elementResponseEntity));

        //## When
        var result = iSendRequestPort.sendRequest(externalPaymentId);

        //## Then
        assertThat(result).usingRecursiveAssertion().isNotNull();

    }

    @Test
    void sendRequestWhenReturnNull() {

        //## Mocks - Objects
        String externalPaymentId = "12345";
        var paymentElementDto = new PaymentElementDto();
        paymentElementDto.setExternalPaymentId(externalPaymentId);

        //## Given
        given(mercadoPagoClient.getOrderDetails(any(), any()))
                .willReturn(ResponseEntity.ok(null));

        //## When
        var result = iSendRequestPort.sendRequest(externalPaymentId);

        //## Then
        assertThat(result).usingRecursiveAssertion().isNull();

    }
}