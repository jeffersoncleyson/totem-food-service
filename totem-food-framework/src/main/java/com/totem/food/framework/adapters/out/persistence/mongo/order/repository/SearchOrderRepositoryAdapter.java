package com.totem.food.framework.adapters.out.persistence.mongo.order.repository;

import com.totem.food.application.ports.in.dtos.order.OrderFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.order.entity.OrderEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.mapper.IOrderEntityMapper;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public class SearchOrderRepositoryAdapter implements ISearchRepositoryPort<OrderFilterDto, List<OrderDomain>> {

    @Repository
    protected interface OrderRepositoryMongoDB extends BaseRepository<OrderEntity, String> {

        @Query("{'name': ?0}")
        List<OrderEntity> findByFilter(String name);
    }

    private final SearchOrderRepositoryAdapter.OrderRepositoryMongoDB repository;
    private final IOrderEntityMapper iOrderEntityMapper;

    @Override
    public List<OrderDomain> findAll(OrderFilterDto filter) {
        return repository.findByFilter(filter.getName()).stream().map(iOrderEntityMapper::toDomain).toList();
    }
}
