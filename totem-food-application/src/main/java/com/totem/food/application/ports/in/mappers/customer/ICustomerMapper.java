package com.totem.food.application.ports.in.mappers.customer;

import com.totem.food.application.ports.in.dtos.customer.CustomerCreateDto;
import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.in.utils.Utils;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.domain.customer.CustomerDomain;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICustomerMapper {

    CustomerDomain toDomain(CustomerDto input);

    CustomerDomain toDomain(CustomerModel input);

    CustomerDto toDto(CustomerModel input);

    CustomerModel toModel(CustomerDomain input);

    @Mapping(source = "password", target = "password", qualifiedByName = "hashingPassword")
    CustomerDomain toDomain(CustomerCreateDto input);

    @Named("hashingPassword")
    default String hashingPassword(String password) {
        return Utils.hash256(password);
    }

}
