package com.totem.food.framework.adapters.out.persistence.mongo.customer.mapper;

import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICustomerEntityMapper {

    CustomerEntity toEntity(CustomerModel input);

    @Mapping(target = "password", ignore = true)
    CustomerModel toModel(CustomerEntity input);
}
