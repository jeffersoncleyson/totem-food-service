package com.totem.food.framework.adapters.out.web.payment.request;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.totem.food.application.exceptions.ExternalCommunicationInvalid;
import com.totem.food.application.ports.in.dtos.payment.PaymentQRCodeDto;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.application.ports.out.web.ISendRequestPort;
import com.totem.food.framework.adapters.out.web.payment.config.PaymentConfigs;
import com.totem.food.framework.adapters.out.web.payment.entity.PaymentResponseEntity;
import com.totem.food.framework.adapters.out.web.payment.mapper.IPaymentRequestMapper;
import com.totem.food.framework.adapters.out.web.payment.mapper.IPaymentResponseMapper;
import com.totem.food.framework.test.utils.TestUtils;
import lombok.SneakyThrows;
import mocks.adapters.out.web.payment.request.PaymentRequestMock;
import mocks.adapters.out.web.payment.request.PaymentResponseEntityMock;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendPaymentExternalRequestAdapterTest {

    @Spy
    private RestTemplate restTemplate;
    @Spy
    private IPaymentRequestMapper iPaymentRequestMapper = Mappers.getMapper(IPaymentRequestMapper.class);
    @Spy
    private IPaymentResponseMapper iPaymentResponseMapper = Mappers.getMapper(IPaymentResponseMapper.class);
    @Mock
    private PaymentConfigs paymentConfigs;

    private ISendRequestPort<PaymentModel, PaymentQRCodeDto> sendRequestPort;
    private AutoCloseable closeable;

    @RegisterExtension
    static WireMockExtension wm = WireMockExtension.newInstance()
            .proxyMode(true)
            .build();

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        sendRequestPort = new SendPaymentExternalRequestAdapter(restTemplate, iPaymentRequestMapper, iPaymentResponseMapper, paymentConfigs);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void sendRequest() {

        //### Given - Objects and Values
        final var pathPaymentGateway = "/payment-gateway";
        final var paymentGatewayURL = "http://localhost".concat(":" + wm.getPort()).concat(pathPaymentGateway);
        final var paymentRequest = PaymentRequestMock.paymentDomain();

        final var paymentResponse = PaymentResponseEntityMock.responseEntity();
        final var json = TestUtils.toJSON(paymentResponse).orElseThrow();

        //### Given - Mocks
        wm.stubFor(WireMock.post(pathPaymentGateway)
                .withHost(WireMock.equalTo("localhost"))
                .willReturn(WireMock.ok(json).withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));
        when(paymentConfigs.getUrl()).thenReturn(paymentGatewayURL);

        //### When
        final var responsePaymentDto = sendRequestPort.sendRequest(paymentRequest);


        //### Then
        verify(iPaymentRequestMapper, times(1)).toEntity(Mockito.any(PaymentModel.class));
        verify(paymentConfigs, times(1)).getUrl();
        verify(restTemplate, times(1)).postForEntity(
                Mockito.any(URI.class),
                Mockito.any(HttpEntity.class),
                Mockito.eq(PaymentResponseEntity.class)
        );
        verify(iPaymentResponseMapper, times(1)).toDto(Mockito.any(PaymentResponseEntity.class));

        assertThat(responsePaymentDto)
                .usingRecursiveComparison()
                .ignoringFields("status", "paymentId")
                .isEqualTo(paymentResponse);
    }

    @Test
    void sendRequestPaymentSystemUnavailable() {

        //### Given - Objects and Values
        final var pathPaymentGateway = "/payment-gateway";
        final var paymentGatewayURL = "http://localhost".concat(":" + wm.getPort()).concat(pathPaymentGateway);
        final var paymentRequest = PaymentRequestMock.paymentDomain();

        //### Given - Mocks
        wm.stubFor(WireMock.post(pathPaymentGateway)
                .withHost(WireMock.equalTo("localhost"))
                .willReturn(WireMock.serverError().withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));
        when(paymentConfigs.getUrl()).thenReturn(paymentGatewayURL);

        //### When
        final var exception = assertThrows(ExternalCommunicationInvalid.class, () -> sendRequestPort.sendRequest(paymentRequest));

        //### Then
        assertEquals("Payment system is unavailable", exception.getMessage());
        verify(iPaymentRequestMapper, times(1)).toEntity(Mockito.any(PaymentModel.class));
        verify(paymentConfigs, times(1)).getUrl();
        verify(restTemplate, times(1)).postForEntity(
                Mockito.any(URI.class),
                Mockito.any(HttpEntity.class),
                Mockito.eq(PaymentResponseEntity.class)
        );
    }

    @Test
    void sendRequestBadRequest() {

        //### Given - Objects and Values
        final var pathPaymentGateway = "/payment-gateway";
        final var paymentGatewayURL = "http://localhost".concat(":" + wm.getPort()).concat(pathPaymentGateway);
        final var paymentRequest = PaymentRequestMock.paymentDomain();

        final var uuid = UUID.randomUUID().toString();
        final var idempotency = new com.github.tomakehurst.wiremock.http.HttpHeader(
                ISendRequestPort.IDEMPOTENCE_KEY,
                uuid
        );
        final var contentType = new com.github.tomakehurst.wiremock.http.HttpHeader(
                org.springframework.http.HttpHeaders.CONTENT_TYPE,
                MediaType.APPLICATION_JSON_VALUE
        );

        //### Given - Mocks
        wm.stubFor(WireMock.post(pathPaymentGateway)
                .withHost(WireMock.equalTo("localhost"))
                .willReturn(WireMock.badRequest()
                        .withHeaders(new com.github.tomakehurst.wiremock.http.HttpHeaders(List.of(idempotency, contentType)))
                ));
        when(paymentConfigs.getUrl()).thenReturn(paymentGatewayURL);

        //### When
        final var exception = assertThrows(ExternalCommunicationInvalid.class, () -> sendRequestPort.sendRequest(paymentRequest));


        //### Then
        assertEquals(String.format(
                "Invalid communication with endpoint [%s] receive status [%s] with idempotence [%s]",
                paymentGatewayURL, HttpStatus.BAD_REQUEST.value(), uuid
        ), exception.getMessage());
        verify(iPaymentRequestMapper, times(1)).toEntity(Mockito.any(PaymentModel.class));
        verify(paymentConfigs, times(1)).getUrl();
        verify(restTemplate, times(1)).postForEntity(
                Mockito.any(URI.class),
                Mockito.any(HttpEntity.class),
                Mockito.eq(PaymentResponseEntity.class)
        );
    }
}