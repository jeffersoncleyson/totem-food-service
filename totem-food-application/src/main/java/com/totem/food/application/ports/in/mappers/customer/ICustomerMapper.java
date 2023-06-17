package com.totem.food.application.ports.in.mappers.customer;

import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.domain.customer.CustomerDomain;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component
public interface ICustomerMapper {

    CustomerDomain toDomain(CustomerDto input);

    CustomerDto toDto(CustomerDomain input);
}
