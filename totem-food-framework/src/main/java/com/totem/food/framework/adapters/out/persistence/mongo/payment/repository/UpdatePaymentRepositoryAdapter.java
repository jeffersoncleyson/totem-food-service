package com.totem.food.framework.adapters.out.persistence.mongo.payment.repository;

import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.domain.payment.PaymentDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.entity.OrderEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.mapper.IOrderEntityMapper;
import com.totem.food.framework.adapters.out.persistence.mongo.payment.entity.PaymentEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.payment.mapper.IPaymentEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Component
public class UpdatePaymentRepositoryAdapter implements IUpdateRepositoryPort<PaymentDomain> {

	@Repository
	protected interface PaymentRepositoryMongoDB extends BaseRepository<PaymentEntity, String> {
	}

	private final PaymentRepositoryMongoDB repository;
	private final IPaymentEntityMapper iPaymentEntityMapper;

	@Override
	public PaymentDomain updateItem(PaymentDomain item) {
		final var entity = iPaymentEntityMapper.toEntity(item);
		final var savedEntity = repository.save(entity);
		return iPaymentEntityMapper.toDomain(savedEntity);
	}

}
