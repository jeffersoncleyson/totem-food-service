package com.totem.food.application.usecases.payment;

import com.totem.food.application.ports.in.dtos.payment.PaymentDto;
import com.totem.food.application.ports.in.mappers.payment.IPaymentMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.ISearchUniqueUseCase;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
@UseCase
public class SearchUniquePaymentUseCase implements ISearchUniqueUseCase<String, Optional<PaymentDto>> {

    private final ISearchUniqueRepositoryPort<Optional<PaymentModel>> iSearchUniqueRepositoryPort;
    private final IPaymentMapper iPaymentMapper;

    @Override
    public Optional<PaymentDto> item(String id) {
        return iSearchUniqueRepositoryPort.findById(id).map(iPaymentMapper::toDto);
    }

}
