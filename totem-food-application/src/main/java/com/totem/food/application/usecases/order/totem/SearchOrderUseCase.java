package com.totem.food.application.usecases.order.totem;

import com.totem.food.application.ports.in.dtos.order.totem.OrderDto;
import com.totem.food.application.ports.in.dtos.order.totem.OrderFilterDto;
import com.totem.food.application.ports.in.mappers.order.totem.IOrderMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.order.totem.OrderModel;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.totem.food.domain.order.enums.OrderStatusEnumDomain.IN_PREPARATION;
import static com.totem.food.domain.order.enums.OrderStatusEnumDomain.READY;
import static com.totem.food.domain.order.enums.OrderStatusEnumDomain.RECEIVED;

@AllArgsConstructor
@UseCase
public class SearchOrderUseCase implements ISearchUseCase<OrderFilterDto, List<OrderDto>> {

    private final IOrderMapper iOrderMapper;
    private final ISearchRepositoryPort<OrderFilterDto, List<OrderModel>> iSearchOrderRepositoryPort;

    @Override
    public List<OrderDto> items(OrderFilterDto filter) {

        if (Boolean.TRUE.equals(filter.getOnlyTreadmill())) {
            return sortedOrderByStatus(filter);
        }

        return getOrders(filter);
    }

    private List<OrderDto> sortedOrderByStatus(OrderFilterDto filter) {

        Map<String, List<OrderDto>> orderDtosMap = getOrders(filter).stream()
                .sorted(Comparator.comparing(OrderDto::getCreateAt))
                .collect(Collectors.groupingBy(OrderDto::getStatus));

        List<OrderDto> orders = new ArrayList<>();
        for (OrderStatusEnumDomain status : List.of(READY, IN_PREPARATION, RECEIVED)) {
            orders.addAll(orderDtosMap.getOrDefault(status.key, new ArrayList<>()));
        }

        return orders;
    }

    private ArrayList<OrderDto> getOrders(OrderFilterDto filter) {
        return iSearchOrderRepositoryPort.findAll(filter)
                .stream()
                .map(iOrderMapper::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
