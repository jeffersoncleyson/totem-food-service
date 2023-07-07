package com.totem.food.framework.adapters.out.persistence.mongo.order.admin.repository;

import com.totem.food.application.ports.in.dtos.order.admin.OrderAdminFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.order.admin.OrderAdminModel;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.order.admin.entity.OrderAdminEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.admin.mapper.IOrderAdminEntityMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class SearchOrderAdminRepositoryAdapter implements ISearchRepositoryPort<OrderAdminFilterDto, List<OrderAdminModel>> {

    @Repository
    protected interface OrderRepositoryMongoDB extends BaseRepository<OrderAdminEntity, String> {

        @Query("{'name': ?0}")
        List<OrderAdminEntity> findByFilter(String name);

        @Query("{'status': {$in: ?0}}")
        List<OrderAdminEntity> findByStatus(Set<String> status);

        List<OrderAdminEntity> findAll();
    }

    private final SearchOrderAdminRepositoryAdapter.OrderRepositoryMongoDB repository;
    private final IOrderAdminEntityMapper iOrderEntityMapper;

    @Override
    public List<OrderAdminModel> findAll(OrderAdminFilterDto filter) {
        if (CollectionUtils.isNotEmpty(filter.getStatus())) {
            final var status = filter.getStatus().stream().map(OrderStatusEnumDomain::from)
                    .map(s -> s.key)
                    .collect(Collectors.toSet());
            return repository.findByStatus(status)
                    .stream()
                    .map(iOrderEntityMapper::toModel)
                    .toList();
        }
        return repository.findAll().stream().map(iOrderEntityMapper::toModel).toList();
    }
}
