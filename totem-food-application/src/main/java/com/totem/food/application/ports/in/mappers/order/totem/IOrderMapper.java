package com.totem.food.application.ports.in.mappers.order.totem;

import com.totem.food.application.ports.in.dtos.order.totem.OrderDto;
import com.totem.food.application.ports.out.persistence.order.totem.OrderModel;
import com.totem.food.domain.order.totem.OrderDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IOrderMapper {

    @Mapping(source = "status.key", target = "status")
    OrderDto toDto(OrderModel input);

    OrderModel toModel(OrderDomain input);

    OrderDomain toDomain(OrderModel input);
}
