package com.totem.food.application.ports.in.mappers.customer;

import com.totem.food.application.ports.in.dtos.customer.CustomerCreateDto;
import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.domain.customer.CustomerDomain;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICustomerMapper {

    CustomerDomain toDomain(CustomerDto input);

    CustomerDto toDto(CustomerDomain input);

    CustomerDomain toDomain(CustomerCreateDto input);
}
