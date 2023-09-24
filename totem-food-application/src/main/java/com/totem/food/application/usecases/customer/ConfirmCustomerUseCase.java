package com.totem.food.application.usecases.customer;

import com.totem.food.application.ports.in.dtos.customer.CustomerConfirmDto;
import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.out.persistence.commons.IConfirmRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IRemoveRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.IConfirmUseCase;
import com.totem.food.application.usecases.commons.IDeleteUseCase;
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
