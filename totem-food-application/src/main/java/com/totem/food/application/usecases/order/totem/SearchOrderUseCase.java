package com.totem.food.application.usecases.order.totem;

import com.totem.food.application.ports.in.dtos.order.totem.OrderDto;
import com.totem.food.application.ports.in.dtos.order.totem.OrderFilterDto;
import com.totem.food.application.ports.in.mappers.order.totem.IOrderMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.domain.order.totem.OrderDomain;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@UseCase
public class SearchOrderUseCase implements ISearchUseCase<OrderFilterDto, List<OrderDto>> {

    private final IOrderMapper iOrderMapper;
    private final ISearchRepositoryPort<OrderFilterDto, List<OrderDomain>> iSearchOrderRepositoryPort;

    @Override
    public List<OrderDto> items(OrderFilterDto filter) {
        return iSearchOrderRepositoryPort.findAll(filter)
                .stream()
                .map(iOrderMapper::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
