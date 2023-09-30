package com.totem.food.application.usecases.customer;

import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.out.persistence.commons.IRemoveRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.IDeleteUseCase;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@UseCase
public class DeleteCustomerUseCase implements IDeleteUseCase<String, CustomerDto> {

    private final IRemoveRepositoryPort<CustomerModel> iRemoveRepositoryPort;

    @Override
    public CustomerDto removeItem(String authorization) {
        iRemoveRepositoryPort.removeItem(authorization);
        return null;
    }
}
