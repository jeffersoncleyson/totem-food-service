package com.totem.food.framework.adapters.out.persistence.mongo.order.totem.repository;

import com.totem.food.application.ports.in.dtos.order.totem.OrderFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.entity.OrderEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.mapper.IOrderEntityMapper;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Component
public class SearchOrderRepositoryAdapter implements ISearchRepositoryPort<OrderFilterDto, List<OrderDomain>> {

    @Repository
    protected interface OrderRepositoryMongoDB extends BaseRepository<OrderEntity, String> {

        @Query("{'customer' :{'$ref' : 'customer' , '$id' : ?0}}")
        List<OrderEntity> findByFilter(ObjectId customerId);

        List<OrderEntity> findAll();
    }

    private final SearchOrderRepositoryAdapter.OrderRepositoryMongoDB repository;
    private final IOrderEntityMapper iOrderEntityMapper;

    @Override
    public List<OrderDomain> findAll(OrderFilterDto filter) {
        return repository.findByFilter(new ObjectId(filter.getCustomerId())).stream().map(iOrderEntityMapper::toDomain).toList();
    }
}
