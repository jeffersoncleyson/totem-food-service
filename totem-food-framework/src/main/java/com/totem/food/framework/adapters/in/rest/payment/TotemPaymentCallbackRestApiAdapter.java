package com.totem.food.framework.adapters.in.rest.payment;

import com.totem.food.application.ports.in.dtos.payment.PaymentFilterDto;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.totem.food.framework.adapters.in.rest.constants.Routes.API_VERSION_1;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.PAYMENT_ORDER_ID;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.TOTEM_PAYMENT_CALLBACK;

@RestController
@RequestMapping(value = API_VERSION_1 + TOTEM_PAYMENT_CALLBACK)
@AllArgsConstructor
public class TotemPaymentCallbackRestApiAdapter {

    private final IUpdateUseCase<PaymentFilterDto, Boolean> iUpdateUseCase;

    @PutMapping(value = PAYMENT_ORDER_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> getItem(
            @PathVariable(name = "orderId") String orderId,
            @RequestHeader String token) {
        final var processed = iUpdateUseCase.updateItem(PaymentFilterDto.builder().orderId(orderId).token(token).build(), "");
        if(Boolean.TRUE.equals(processed)) return ResponseEntity.noContent().build();
        return ResponseEntity.badRequest().build();
    }
}