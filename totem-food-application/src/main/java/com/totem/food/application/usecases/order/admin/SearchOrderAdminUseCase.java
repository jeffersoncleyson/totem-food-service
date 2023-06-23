package com.totem.food.application.usecases.order.admin;

import com.totem.food.application.ports.in.dtos.order.admin.OrderAdminDto;
import com.totem.food.application.ports.in.dtos.order.admin.OrderAdminFilterDto;
import com.totem.food.application.ports.in.mappers.order.admin.IOrderAdminMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.domain.order.admin.OrderAdminDomain;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@UseCase
public class SearchOrderAdminUseCase implements ISearchUseCase<OrderAdminFilterDto, List<OrderAdminDto>> {

    private final IOrderAdminMapper iOrderAdminMapper;
    private final ISearchRepositoryPort<OrderAdminFilterDto, List<OrderAdminDomain>> iSearchOrderRepositoryPort;

    @Override
    public List<OrderAdminDto> items(OrderAdminFilterDto filter) {
        return iSearchOrderRepositoryPort.findAll(filter)
                .stream()
                .map(domain -> {
                    domain.calcWaitTime();
                    return domain;
                })
                .map(iOrderAdminMapper::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
