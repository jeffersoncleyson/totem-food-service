package com.totem.food.application.ports.in.mappers.customer;

import com.totem.food.application.ports.in.dtos.customer.CustomerCreateDto;
import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.in.utils.Utils;
import com.totem.food.domain.customer.CustomerDomain;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICustomerMapper {

    CustomerDomain toDomain(CustomerDto input);

    CustomerDto toDto(CustomerDomain input);

    @Mapping(source = "password", target = "password", qualifiedByName = "hashingPassword")
    CustomerDomain toDomain(CustomerCreateDto input);

    @Named("hashingPassword")
    default String hashingPassword(String password) {
        return Utils.hash256(password);
    }

}
