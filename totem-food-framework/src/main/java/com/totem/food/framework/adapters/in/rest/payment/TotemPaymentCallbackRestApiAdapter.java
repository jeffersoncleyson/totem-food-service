package com.totem.food.framework.adapters.in.rest.payment;

import com.totem.food.application.ports.in.dtos.payment.PaymentFilterDto;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import com.totem.food.domain.payment.PaymentDomain;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/totem/payment/callback")
@AllArgsConstructor
public class TotemPaymentCallbackRestApiAdapter {

    private final IUpdateUseCase<PaymentFilterDto, Boolean> iUpdateUseCase;

    @PutMapping(value = "/order/{orderId}/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> getItem(
            @PathVariable(name = "orderId") String orderId,
            @PathVariable(name = "customerId") String customerId,
            @RequestHeader String token) {
        final var processed = iUpdateUseCase.updateItem(new PaymentFilterDto(orderId, customerId, token), "");
        if(Boolean.TRUE.equals(processed)) return ResponseEntity.noContent().build();
        return ResponseEntity.badRequest().build();
    }
}