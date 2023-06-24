package com.totem.food.framework.adapters.out.persistence.mongo.payment.repository;

import com.totem.food.application.ports.in.dtos.payment.PaymentFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.domain.payment.PaymentDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.payment.entity.PaymentEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.payment.mapper.IPaymentEntityMapper;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Component
public class SearchPaymentRepositoryAdapter implements ISearchRepositoryPort<PaymentFilterDto, PaymentDomain> {

	@Repository
	protected interface PaymentRepositoryMongoDB extends BaseRepository<PaymentEntity, String> {
		@Query("{'order' :{'$ref' : 'order' , '$id' : ?0}, 'token' : ?1}")
		PaymentEntity findByFilter(ObjectId order, String token);
	}

	private final PaymentRepositoryMongoDB repository;
	private final IPaymentEntityMapper iPaymentMapper;

	@Override
	public PaymentDomain findAll(PaymentFilterDto item) {
		final var entity = repository.findByFilter(new ObjectId(item.getOrderId()), item.getToken());
		return iPaymentMapper.toDomain(entity);
	}

}
