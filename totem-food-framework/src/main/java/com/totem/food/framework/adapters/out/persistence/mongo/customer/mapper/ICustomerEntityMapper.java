package com.totem.food.framework.adapters.out.persistence.mongo.customer.mapper;

import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICustomerEntityMapper {

    CustomerEntity toEntity(CustomerDomain input);

    CustomerDomain toDomain(CustomerEntity input);
}
