package com.totem.food.framework.adapters.out.persistence.mongo.payment.mapper;

import com.totem.food.domain.payment.PaymentDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.payment.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IPaymentEntityMapper {

	PaymentEntity toEntity(PaymentDomain input);

	PaymentDomain toDomain(PaymentEntity input);

}
