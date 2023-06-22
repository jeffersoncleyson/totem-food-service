package com.totem.food.application.usecases.payment;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.payment.PaymentFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.application.usecases.commons.IUpdateStatusUseCase;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import com.totem.food.domain.payment.PaymentDomain;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@UseCase
public class UpdatePaymentUseCase implements IUpdateUseCase<PaymentFilterDto, Boolean> {

    private final ISearchRepositoryPort<PaymentFilterDto, PaymentDomain> iSearchRepositoryPort;
    private final IUpdateRepositoryPort<PaymentDomain> iUpdateRepositoryPort;

    @Override
    public Boolean updateItem(PaymentFilterDto item, String id) {

        final var paymentDomain = iSearchRepositoryPort.findAll(item);

        if(Objects.nonNull(paymentDomain)){

            if(paymentDomain.getStatus().equals(PaymentDomain.PaymentStatus.COMPLETED)) return Boolean.TRUE;

            paymentDomain.updateStatus(PaymentDomain.PaymentStatus.COMPLETED);
            iUpdateRepositoryPort.updateItem(paymentDomain);
            return Boolean.TRUE;
        }

        throw new ElementNotFoundException(
                String.format("Payment with filters orderId: [%s] customerId: [%s] token: [%s] not found",
                        item.getOrderId(),
                        item.getCustomerId(),
                        item.getToken()
                )
        );
    }
}