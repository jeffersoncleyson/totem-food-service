package com.totem.food.application.usecases.customer;

import com.totem.food.application.ports.in.dtos.customer.CustomerConfirmDto;
import com.totem.food.application.ports.out.persistence.commons.IConfirmRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.IConfirmUseCase;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@UseCase
public class ConfirmCustomerUseCase implements IConfirmUseCase<Boolean, CustomerConfirmDto> {

    private final IConfirmRepositoryPort<CustomerConfirmDto, Boolean> iConfirmRepositoryPort;

    @Override
    public Boolean confirm(CustomerConfirmDto item) {
        return iConfirmRepositoryPort.confirmItem(item);
    }
}
