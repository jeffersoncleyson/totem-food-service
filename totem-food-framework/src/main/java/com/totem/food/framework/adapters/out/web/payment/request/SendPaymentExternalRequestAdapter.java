package com.totem.food.framework.adapters.out.web.payment.request;

import com.totem.food.application.exceptions.ExternalCommunicationInvalid;
import com.totem.food.application.ports.in.dtos.payment.PaymentQRCodeDto;
import com.totem.food.application.ports.out.web.ISendRequestPort;
import com.totem.food.domain.payment.PaymentDomain;
import com.totem.food.framework.adapters.out.web.payment.config.PaymentConfigs;
import com.totem.food.framework.adapters.out.web.payment.entity.PaymentResponseEntity;
import com.totem.food.framework.adapters.out.web.payment.mapper.IPaymentRequestMapper;
import com.totem.food.framework.adapters.out.web.payment.mapper.IPaymentResponseMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

@AllArgsConstructor
@Component
public class SendPaymentExternalRequestAdapter implements ISendRequestPort<PaymentDomain, PaymentQRCodeDto> {

    private final RestTemplate restTemplate;
    private final IPaymentRequestMapper iPaymentRequestMapper;
    private final IPaymentResponseMapper iPaymentResponseMapper;
    private final PaymentConfigs paymentConfigs;

    @Override
    public PaymentQRCodeDto sendRequest(PaymentDomain item) {

        final var entity = iPaymentRequestMapper.toEntity(item);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> body = new HttpEntity<>(entity, headers);

        final var uri = URI.create(paymentConfigs.getUrl());
        ResponseEntity<PaymentResponseEntity> responseEntity;
        try {
            responseEntity = restTemplate.postForEntity(uri, body, PaymentResponseEntity.class);
        }catch (Exception ex){
            throw new ExternalCommunicationInvalid("Payment system is unavailable");
        }


        if(responseEntity.getStatusCode().is2xxSuccessful()){
            return iPaymentResponseMapper.toDto(responseEntity.getBody());
        }

        final var idempotence = Optional.of(responseEntity)
                .map(HttpEntity::getHeaders)
                .map( h -> h.getFirst(IDEMPOTENCE_KEY))
                .orElse("");

        throw new ExternalCommunicationInvalid(
                String.format(
                        "Invalid communication with endpoint [%s] receive status [%s] with idempotence [%s]",
                        uri, responseEntity.getStatusCodeValue(), idempotence
                )
        );
    }
}
