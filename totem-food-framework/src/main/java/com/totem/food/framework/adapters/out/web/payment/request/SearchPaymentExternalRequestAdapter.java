package com.totem.food.framework.adapters.out.web.payment.request;

import com.totem.food.application.ports.in.dtos.payment.PaymentElementDto;
import com.totem.food.application.ports.out.web.ISendRequestPort;
import com.totem.food.framework.adapters.out.web.payment.client.MercadoPagoClient;
import com.totem.food.framework.adapters.out.web.payment.mapper.IPaymentResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchPaymentExternalRequestAdapter implements ISendRequestPort<String, PaymentElementDto> {

    @Value("${TOKEN}")
    private String token;

    private final IPaymentResponseMapper iPaymentResponseMapper;
    private final MercadoPagoClient mercadoPagoClient;

    @Override
    public PaymentElementDto sendRequest(String externalReference) {

        var elementEntity = mercadoPagoClient.getOrderDetails(token, externalReference).getBody();

        iPaymentResponseMapper.toDto(elementEntity);

        return iPaymentResponseMapper.toDto(elementEntity);
    }
}
