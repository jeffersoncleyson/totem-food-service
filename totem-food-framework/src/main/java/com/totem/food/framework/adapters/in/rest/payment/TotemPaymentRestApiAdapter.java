package com.totem.food.framework.adapters.in.rest.payment;

import com.totem.food.application.ports.in.dtos.payment.PaymentCreateDto;
import com.totem.food.application.ports.in.dtos.payment.PaymentDto;
import com.totem.food.application.ports.in.dtos.payment.PaymentQRCodeDto;
import com.totem.food.application.ports.in.rest.ICreateRestApiPort;
import com.totem.food.application.ports.in.rest.ISearchUniqueRestApiPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.application.usecases.commons.ISearchUniqueUseCase;
import com.totem.food.framework.adapters.in.rest.constants.Routes;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = Routes.VERSION_1 + Routes.TOTEM_PAYMENT)
@AllArgsConstructor
public class TotemPaymentRestApiAdapter implements ICreateRestApiPort<PaymentCreateDto, ResponseEntity<PaymentQRCodeDto>>,
        ISearchUniqueRestApiPort<String, ResponseEntity<PaymentDto>> {

    private final ICreateUseCase<PaymentCreateDto, PaymentQRCodeDto> iCreateUseCase;
    private final ISearchUniqueUseCase<String, Optional<PaymentDto>> iSearchUniqueUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<PaymentQRCodeDto> create(@RequestBody PaymentCreateDto item) {
        final var created = iCreateUseCase.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping(value = Routes.PAYMENT_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<PaymentDto> getById(@PathVariable String paymentId) {
        return iSearchUniqueUseCase.item(paymentId).map(payment -> ResponseEntity.status(HttpStatus.OK).body(payment))
                .orElse(ResponseEntity.notFound().build());
    }
}