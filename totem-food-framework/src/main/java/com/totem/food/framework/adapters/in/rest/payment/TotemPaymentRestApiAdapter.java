package com.totem.food.framework.adapters.in.rest.payment;

import com.totem.food.application.ports.in.dtos.payment.PaymentCreateDto;
import com.totem.food.application.ports.in.dtos.payment.PaymentDto;
import com.totem.food.application.ports.in.dtos.payment.PaymentQRCodeDto;
import com.totem.food.application.ports.in.rest.ICreateRestApiPort;
import com.totem.food.application.ports.in.rest.ISearchUniqueRestApiPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.application.usecases.commons.ISearchUniqueUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.totem.food.framework.adapters.in.rest.constants.Routes.API_VERSION_1;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.PAYMENT_ID;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.TOTEM_PAYMENT;

@RestController
@RequestMapping(value = API_VERSION_1 + TOTEM_PAYMENT)
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

    @GetMapping(value = PAYMENT_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<PaymentDto> getById(@PathVariable String paymentId) {
        return iSearchUniqueUseCase.item(paymentId).map(payment -> ResponseEntity.status(HttpStatus.OK).body(payment))
                .orElse(ResponseEntity.notFound().build());
    }
}