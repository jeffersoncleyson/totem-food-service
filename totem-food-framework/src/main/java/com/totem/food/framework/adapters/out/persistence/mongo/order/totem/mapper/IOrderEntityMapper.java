package com.totem.food.framework.adapters.out.persistence.mongo.order.totem.mapper;

import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IOrderEntityMapper {

	@Mapping(source = "status.key", target = "status")
	OrderEntity toEntity(OrderDomain input);

	OrderDomain toDomain(OrderEntity input);
}
