package com.totem.food.framework.adapters.out.web.payment.request;

import com.totem.food.application.ports.in.dtos.payment.PaymentElementDto;
import com.totem.food.application.ports.out.web.ISendRequestPort;
import com.totem.food.framework.adapters.out.web.payment.client.MercadoPagoClient;
import com.totem.food.framework.adapters.out.web.payment.entity.ElementResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SearchPaymentExternalRequestAdapter implements ISendRequestPort<String, PaymentElementDto> {

    @Value("${payment.store_token_id}")
    private String token;

    private final MercadoPagoClient mercadoPagoClient;

    @Override
    public PaymentElementDto sendRequest(String externalReference) {

        var elementEntity = mercadoPagoClient.getOrderDetails(token, externalReference).getBody();

        if (Objects.isNull(elementEntity) || Objects.isNull(elementEntity.getData())) {
            return null;
        }

        return getPaymentElementDto(elementEntity);
    }

    private PaymentElementDto getPaymentElementDto(ElementResponseEntity elementEntity) {
        return PaymentElementDto.builder()
                .externalReference(elementEntity.getData().get(0).getExternalReference())
                .orderStatus(elementEntity.getData().get(0).getOrderStatus())
                .totalPayment(elementEntity.getData().get(0).getTotalPayment())
                .updatePayment(elementEntity.getData().get(0).getUpdatePayment())
                .build();
    }

}
