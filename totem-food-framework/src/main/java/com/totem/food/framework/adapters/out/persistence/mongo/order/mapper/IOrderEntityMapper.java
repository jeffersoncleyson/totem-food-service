package com.totem.food.framework.adapters.out.persistence.mongo.order.mapper;

import com.totem.food.framework.adapters.out.persistence.mongo.order.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IOrderEntityMapper {

    OrderEntity toEntity(OrderDomain input);

    OrderDomain toDomain(OrderEntity input);
}
