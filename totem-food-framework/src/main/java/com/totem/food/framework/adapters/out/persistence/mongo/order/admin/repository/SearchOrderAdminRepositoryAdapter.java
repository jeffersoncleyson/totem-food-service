package com.totem.food.framework.adapters.out.persistence.mongo.order.admin.repository;

import com.totem.food.application.ports.in.dtos.order.admin.OrderAdminFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.domain.order.admin.OrderAdminDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.order.admin.entity.OrderAdminEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.admin.mapper.IOrderAdminEntityMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Component
public class SearchOrderAdminRepositoryAdapter implements ISearchRepositoryPort<OrderAdminFilterDto, List<OrderAdminDomain>> {

    @Repository
    protected interface OrderRepositoryMongoDB extends BaseRepository<OrderAdminEntity, String> {

        @Query("{'name': ?0}")
        List<OrderAdminEntity> findByFilter(String name);

        List<OrderAdminEntity> findByStatus(String status);

        List<OrderAdminEntity> findAll();
    }

    private final SearchOrderAdminRepositoryAdapter.OrderRepositoryMongoDB repository;
    private final IOrderAdminEntityMapper iOrderEntityMapper;

    @Override
    public List<OrderAdminDomain> findAll(OrderAdminFilterDto filter) {
        if(StringUtils.isNotEmpty(filter.getStatus()))
            return repository.findByStatus(filter.getStatus()).stream().map(iOrderEntityMapper::toDomain).toList();
        return repository.findAll().stream().map(iOrderEntityMapper::toDomain).toList();
    }
}
