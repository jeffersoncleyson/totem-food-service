package com.totem.food.framework.adapters.out.web.payment.request;

import com.totem.food.application.ports.in.dtos.payment.PaymentQRCodeDto;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.application.ports.out.web.ISendRequestPort;
import com.totem.food.framework.adapters.in.rest.payment.entity.PaymentItemsRequestEntity;
import com.totem.food.framework.adapters.in.rest.payment.entity.PaymentRequestEntity;
import com.totem.food.framework.adapters.out.web.payment.client.MercadoPagoClient;
import com.totem.food.framework.adapters.out.web.payment.mapper.IPaymentResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SendPaymentExternalRequestAdapter implements ISendRequestPort<PaymentModel, PaymentQRCodeDto> {

    @Value("${payment.store_id}")
    private String posId;

    @Value("${payment.store_user_id}")
    private String userId;

    @Value("${payment.store_token_id}")
    private String token;

    @Value("${payment.callback}")
    private String paymentCallback;
    
    private static final ZonedDateTime DURATION_QR_CODE = ZonedDateTime.now().plusHours(1);

    private final IPaymentResponseMapper iPaymentResponseMapper;
    private final MercadoPagoClient mercadoPagoClient;

    @Override
    public PaymentQRCodeDto sendRequest(PaymentModel item) {

        var paymentRequest = getPaymentRequestEntity(item);

        var paymentResponse = mercadoPagoClient.createOrder(token, userId, posId, paymentRequest).getBody();

        return iPaymentResponseMapper.toDto(paymentResponse);
    }

    private PaymentRequestEntity getPaymentRequestEntity(PaymentModel item) {
        return PaymentRequestEntity.builder()
                .externalReference(item.getId())
                .totalAmount(BigDecimal.valueOf(item.getPrice()))
                .items(getItemsRequest(item))
                .title("Atendimento via Totem")
                .description("Pedido realizado via auto atendimento Totem")
                .expirationDate(DURATION_QR_CODE)
                .notificationUrl(paymentCallback)
                .build();
    }

    private List<PaymentItemsRequestEntity> getItemsRequest(PaymentModel item) {
        return List.of(PaymentItemsRequestEntity.builder()
                .skuNumber(item.getId())
                .category("Alimentos")
                .title("Totem Food Service")
                .description("Pedido via Totem")
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(item.getPrice()))
                .unitMeasure(String.valueOf(item.getPrice()))
                .totalAmount(BigDecimal.valueOf(item.getPrice()))
                .build());
    }

}