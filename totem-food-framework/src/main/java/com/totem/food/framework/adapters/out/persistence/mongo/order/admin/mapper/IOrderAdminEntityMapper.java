package com.totem.food.framework.adapters.out.persistence.mongo.order.admin.mapper;

import com.totem.food.application.ports.out.persistence.order.admin.OrderAdminModel;
import com.totem.food.domain.order.admin.OrderAdminDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.order.admin.entity.OrderAdminEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IOrderAdminEntityMapper {

    OrderAdminEntity toEntity(OrderAdminModel input);

    OrderAdminModel toModel(OrderAdminEntity input);
}
