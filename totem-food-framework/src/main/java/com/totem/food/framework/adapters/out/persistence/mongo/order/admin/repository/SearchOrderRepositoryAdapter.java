package com.totem.food.framework.adapters.out.persistence.mongo.order.admin.repository;

import com.totem.food.application.ports.in.dtos.order.admin.OrderAdminFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.domain.order.admin.OrderAdminDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.order.admin.entity.OrderAdminEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.admin.mapper.IOrderEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Component
public class SearchOrderRepositoryAdapter implements ISearchRepositoryPort<OrderAdminFilterDto, List<OrderAdminDomain>> {

    @Repository
    protected interface OrderRepositoryMongoDB extends BaseRepository<OrderAdminEntity, String> {

        @Query("{'name': ?0}")
        List<OrderAdminEntity> findByFilter(String name);

        List<OrderAdminEntity> findAll();
    }

    private final SearchOrderRepositoryAdapter.OrderRepositoryMongoDB repository;
    private final IOrderEntityMapper iOrderEntityMapper;

    @Override
    public List<OrderAdminDomain> findAll(OrderAdminFilterDto filter) {
        return repository.findAll().stream().map(iOrderEntityMapper::toDomain).toList();
    }
}
