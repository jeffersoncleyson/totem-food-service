package com.totem.food.framework.adapters.in.rest.payment.adapter;

import com.totem.food.application.ports.in.dtos.payment.PaymentCallbackDto;
import com.totem.food.application.ports.in.dtos.payment.PaymentFilterDto;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

import static com.totem.food.domain.payment.PaymentDomain.PaymentStatus.PENDING;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.API_VERSION_1;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.TOTEM_PAYMENT_CALLBACK;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = API_VERSION_1 + TOTEM_PAYMENT_CALLBACK)
public class TotemPaymentCallbackRestApiAdapter {

    private final IUpdateUseCase<PaymentFilterDto, Boolean> iUpdateUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> callbackPayment(@RequestBody PaymentCallbackDto paymentCallbackDto) {

        var filter = PaymentFilterDto.builder()
                .status(PENDING.name())
                .timeLastOrders(ZonedDateTime.now().minusMinutes(30))
                .build();

        final var processed = iUpdateUseCase.updateItem(filter, "");

        if (Boolean.TRUE.equals(processed)) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.noContent().build();
    }

}