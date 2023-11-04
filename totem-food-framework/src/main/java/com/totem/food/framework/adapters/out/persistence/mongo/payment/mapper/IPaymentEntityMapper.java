package com.totem.food.framework.adapters.out.persistence.mongo.payment.mapper;

import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.framework.adapters.out.persistence.mongo.payment.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IPaymentEntityMapper {

    @Mapping(source = "status.key", target = "status")
    PaymentEntity toEntity(PaymentModel input);

    PaymentModel toModel(PaymentEntity input);

    List<PaymentModel> toModel(List<PaymentEntity> input);

}
