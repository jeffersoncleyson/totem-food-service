package com.totem.food.framework.adapters.out.web.payment.mapper;

import com.totem.food.application.ports.in.dtos.payment.PaymentElementDto;
import com.totem.food.application.ports.in.dtos.payment.PaymentQRCodeDto;
import com.totem.food.framework.adapters.out.web.payment.entity.ElementPaymentResponseEntity;
import com.totem.food.framework.adapters.out.web.payment.entity.ElementResponseEntity;
import com.totem.food.framework.adapters.out.web.payment.entity.PaymentResponseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IPaymentResponseMapper {

    PaymentQRCodeDto toDto(PaymentResponseEntity input);

    @Mapping(source = "data.externalReference", target = "externalReference")
    @Mapping(source = "data.orderStatus", target = "orderStatus")
    @Mapping(source = "data.totalPayment", target = "totalPayment")
    @Mapping(source = "data.updatePayment", target = "updatePayment")
    @Mapping(target = "externalPaymentId", expression = "java(mapPaymentsToExternalPaymentId(input.getData().getPayments()))")
    PaymentElementDto toDto(ElementResponseEntity input);

    default String mapPaymentsToExternalPaymentId(List<ElementPaymentResponseEntity> payments) {
        return payments.stream().findFirst().toString();
    }

}
