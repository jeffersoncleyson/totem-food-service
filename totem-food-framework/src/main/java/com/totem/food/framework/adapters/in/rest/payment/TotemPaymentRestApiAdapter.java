package com.totem.food.framework.adapters.in.rest.payment;

import com.totem.food.application.ports.in.dtos.payment.PaymentCreateDto;
import com.totem.food.application.ports.in.dtos.payment.PaymentDto;
import com.totem.food.application.ports.in.rest.ICreateRestApiPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/totem/payment")
@AllArgsConstructor
public class TotemPaymentRestApiAdapter implements ICreateRestApiPort<PaymentCreateDto, ResponseEntity<PaymentDto>> {

    private final ICreateUseCase<PaymentCreateDto, PaymentDto> iCreateUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<PaymentDto> create(@RequestBody PaymentCreateDto item) {
        final var created = iCreateUseCase.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}