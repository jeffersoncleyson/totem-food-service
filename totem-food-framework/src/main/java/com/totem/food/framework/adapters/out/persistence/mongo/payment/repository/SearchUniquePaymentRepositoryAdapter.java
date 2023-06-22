package com.totem.food.framework.adapters.out.persistence.mongo.payment.repository;

import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.domain.payment.PaymentDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.payment.entity.PaymentEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.payment.mapper.IPaymentEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@AllArgsConstructor
@Component
public class SearchUniquePaymentRepositoryAdapter implements ISearchUniqueRepositoryPort<Optional<PaymentDomain>> {

	@Repository
	protected interface PaymentRepositoryMongoDB extends BaseRepository<PaymentEntity, String> {
	}

	private final PaymentRepositoryMongoDB repository;
	private final IPaymentEntityMapper iPaymentMapper;

	@Override
	public Optional<PaymentDomain> findById(String id) {
		return repository.findById(id).map(iPaymentMapper::toDomain);
	}

}
