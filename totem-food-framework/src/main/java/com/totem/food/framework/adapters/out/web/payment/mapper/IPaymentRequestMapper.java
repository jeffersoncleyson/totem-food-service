package com.totem.food.framework.adapters.out.web.payment.mapper;

import com.totem.food.domain.payment.PaymentDomain;
import com.totem.food.framework.adapters.out.web.payment.entity.PaymentRequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IPaymentRequestMapper {

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "order.price", target = "price")
    PaymentRequestEntity toEntity(PaymentDomain input);

}
