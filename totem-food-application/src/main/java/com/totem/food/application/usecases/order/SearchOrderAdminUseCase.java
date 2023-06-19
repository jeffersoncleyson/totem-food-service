package com.totem.food.application.usecases.order;

import com.totem.food.application.ports.in.dtos.order.OrderAdminDto;
import com.totem.food.application.ports.in.dtos.order.OrderFilterDto;
import com.totem.food.application.ports.in.mappers.order.IOrderAdminMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.domain.order.OrderAdminDomain;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@UseCase
public class SearchOrderAdminUseCase implements ISearchUseCase<OrderFilterDto, List<OrderAdminDto>> {

    private final IOrderAdminMapper iOrderAdminMapper;
    private final ISearchRepositoryPort<OrderFilterDto, List<OrderAdminDomain>> iSearchOrderRepositoryPort;

    @Override
    public List<OrderAdminDto> items(OrderFilterDto filter) {
        return iSearchOrderRepositoryPort.findAll(filter)
                .stream()
                .map(iOrderAdminMapper::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
