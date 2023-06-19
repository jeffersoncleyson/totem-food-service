package com.totem.food.application.ports.in.mappers.order;

import com.totem.food.application.ports.in.dtos.order.OrderAdminDto;
import com.totem.food.domain.order.OrderAdminDomain;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IOrderAdminMapper {

    OrderAdminDomain toDomain(OrderAdminDto input);

    OrderAdminDto toDto(OrderAdminDomain input);
}
