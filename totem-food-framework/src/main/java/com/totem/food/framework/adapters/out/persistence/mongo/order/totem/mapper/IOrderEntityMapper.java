package com.totem.food.framework.adapters.out.persistence.mongo.order.totem.mapper;

import com.totem.food.application.ports.out.persistence.order.totem.OrderModel;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IOrderEntityMapper {

	@Mapping(source = "status.key", target = "status")
	@Mapping(source = "customer.cpf", target = "cpf")
	OrderEntity toEntity(OrderModel input);

	@Mapping(source = "cpf", target = "customer.cpf")
	OrderModel toModel(OrderEntity input);
}
