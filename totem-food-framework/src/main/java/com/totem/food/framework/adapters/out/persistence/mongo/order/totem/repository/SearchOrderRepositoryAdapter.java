package com.totem.food.framework.adapters.out.persistence.mongo.order.totem.repository;

import com.totem.food.application.ports.in.dtos.order.totem.OrderFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.entity.OrderEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.mapper.IOrderEntityMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
    }

    private final SearchOrderRepositoryAdapter.OrderRepositoryMongoDB repository;
    private final IOrderEntityMapper iOrderEntityMapper;

    @Override
    public List<OrderDomain> findAll(OrderFilterDto filter) {
        if(StringUtils.isNotEmpty(filter.getCustomerId()))
            return repository.findByFilter(new ObjectId(filter.getCustomerId())).stream().map(iOrderEntityMapper::toDomain).toList();
        if(StringUtils.isNotEmpty(filter.getOrderId())){
            return repository.findById(filter.getOrderId()).map(iOrderEntityMapper::toDomain)
                    .map(List::of)
                    .orElse(List.of());
        }
        return List.of();
    }
}
