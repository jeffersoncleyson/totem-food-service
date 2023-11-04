package com.totem.food.application.ports.in.mappers.payment;

import com.totem.food.application.ports.in.dtos.payment.PaymentCreateDto;
import com.totem.food.application.ports.in.dtos.payment.PaymentDto;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.domain.payment.PaymentDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IPaymentMapper {

    PaymentDomain toDomain(PaymentCreateDto input);

    @Mapping(source = "qrcodeBase64", target = "qrcodeBase64")
    PaymentDto toDto(PaymentModel input);

    PaymentDomain toDomain(PaymentModel input);

    PaymentModel toModel(PaymentDomain input);

    @Mapping(source = "qrcodeBase64", target = "qrcodeBase64")
    PaymentModel toModel(PaymentDto input);

}
