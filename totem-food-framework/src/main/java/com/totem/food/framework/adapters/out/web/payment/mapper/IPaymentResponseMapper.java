package com.totem.food.framework.adapters.out.web.payment.mapper;

import com.totem.food.application.ports.in.dtos.payment.PaymentQRCodeDto;
import com.totem.food.framework.adapters.out.web.payment.entity.PaymentResponseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IPaymentResponseMapper {

    PaymentQRCodeDto toDto(PaymentResponseEntity input);

}
