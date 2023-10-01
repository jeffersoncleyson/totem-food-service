package com.totem.food.framework.adapters.out.persistence.mongo.order.totem.repository;

import com.totem.food.application.ports.in.dtos.order.totem.OrderFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.order.totem.OrderModel;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.entity.OrderEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.mapper.IOrderEntityMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class SearchOrderRepositoryAdapter implements ISearchRepositoryPort<OrderFilterDto, List<OrderModel>> {

    @Repository
    protected interface OrderRepositoryMongoDB extends BaseRepository<OrderEntity, String> {

        @Query("{'cpf' : ?0}")
        List<OrderEntity> findByFilter(String cpf);

        @Query("{'status': {$in: ?0}}")
        List<OrderEntity> findByStatus(Set<String> status);

    }

    private final SearchOrderRepositoryAdapter.OrderRepositoryMongoDB repository;
    private final IOrderEntityMapper iOrderEntityMapper;

    @Override
    public List<OrderModel> findAll(OrderFilterDto filter) {

        if (StringUtils.isNotEmpty(filter.getCpf()))
            return repository.findByFilter(filter.getCpf())
                    .stream()
                    .map(iOrderEntityMapper::toModel)
                    .toList();

        if (StringUtils.isNotEmpty(filter.getOrderId())) {
            return repository.findById(filter.getOrderId()).map(iOrderEntityMapper::toModel)
                    .map(List::of)
                    .orElse(List.of());
        }

        if (CollectionUtils.isNotEmpty(filter.getStatus())) {
            final var status = filter.getStatus()
                    .stream()
                    .map(OrderStatusEnumDomain::from)
                    .map(s -> s.key)
                    .collect(Collectors.toSet());
            return repository.findByStatus(status)
                    .stream()
                    .map(iOrderEntityMapper::toModel).toList();
        }

        return List.of();
    }
}
