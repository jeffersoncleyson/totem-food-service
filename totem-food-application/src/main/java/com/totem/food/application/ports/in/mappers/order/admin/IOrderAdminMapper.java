package com.totem.food.application.ports.in.mappers.order.admin;

import com.totem.food.application.ports.in.dtos.order.admin.OrderAdminDto;
import com.totem.food.application.ports.out.persistence.order.admin.OrderAdminModel;
import com.totem.food.domain.order.admin.OrderAdminDomain;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IOrderAdminMapper {

    OrderAdminDomain toDomain(OrderAdminDto input);

    OrderAdminDomain toDomain(OrderAdminModel input);

    OrderAdminDto toDto(OrderAdminModel input);

    OrderAdminDto toDto(OrderAdminDomain input);
}
