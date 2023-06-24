package com.totem.food.framework.adapters.in.rest.payment;

import com.totem.food.application.ports.in.dtos.payment.PaymentFilterDto;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import com.totem.food.framework.adapters.in.rest.constants.Routes;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Routes.VERSION_1 + Routes.TOTEM_PAYMENT_CALLBACK)
@AllArgsConstructor
public class TotemPaymentCallbackRestApiAdapter {

    private final IUpdateUseCase<PaymentFilterDto, Boolean> iUpdateUseCase;

    @PutMapping(value = Routes.PAYMENT_ORDER_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> getItem(
            @PathVariable(name = "orderId") String orderId,
            @RequestHeader String token) {
        final var processed = iUpdateUseCase.updateItem(new PaymentFilterDto(orderId, token), "");
        if(Boolean.TRUE.equals(processed)) return ResponseEntity.noContent().build();
        return ResponseEntity.badRequest().build();
    }
}