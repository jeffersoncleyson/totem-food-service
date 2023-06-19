package com.totem.food.framework.adapters.out.persistence.mongo.order.mapper;

import com.totem.food.domain.order.OrderAdminDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.order.entity.OrderAdminEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IOrderEntityMapper {

    OrderAdminEntity toEntity(OrderAdminDomain input);

    OrderAdminDomain toDomain(OrderAdminEntity input);
}
