package com.totem.food.framework.adapters.out.persistence.mongo.payment.repository;

import com.totem.food.application.ports.in.dtos.payment.PaymentFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.payment.entity.PaymentEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.payment.mapper.IPaymentEntityMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Component
public class SearchPaymentRepositoryAdapter implements ISearchRepositoryPort<PaymentFilterDto, PaymentModel> {

	@Repository
	protected interface PaymentRepositoryMongoDB extends BaseRepository<PaymentEntity, String> {
		@Query("{'order' :{'$ref' : 'order' , '$id' : ?0}, 'token' : ?1}")
		PaymentEntity findByFilter(ObjectId order, String token);

		@Query("{'order' :{'$ref' : 'order' , '$id' : ?0}, 'status' : ?1}")
		PaymentEntity findPaymentByOrderAndStatus(ObjectId order, String status);
	}

	private final PaymentRepositoryMongoDB repository;
	private final IPaymentEntityMapper iPaymentMapper;

	@Override
	public PaymentModel findAll(PaymentFilterDto item) {
		if(StringUtils.isNotEmpty(item.getOrderId()) && StringUtils.isNotEmpty(item.getToken())) {
			final var entity = repository.findByFilter(new ObjectId(item.getOrderId()), item.getToken());
			return iPaymentMapper.toModel(entity);
		} else if(StringUtils.isNotEmpty(item.getOrderId()) && StringUtils.isNotEmpty(item.getStatus())){
			final var entity = repository.findPaymentByOrderAndStatus(new ObjectId(item.getOrderId()), item.getStatus());
			return iPaymentMapper.toModel(entity);
		}
		return null;
	}

}
