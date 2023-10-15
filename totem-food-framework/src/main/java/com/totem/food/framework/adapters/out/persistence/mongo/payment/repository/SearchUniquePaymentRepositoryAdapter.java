package com.totem.food.framework.adapters.out.persistence.mongo.payment.repository;

import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.payment.entity.PaymentEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.payment.mapper.IPaymentEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Component
public class SearchUniquePaymentRepositoryAdapter implements ISearchUniqueRepositoryPort<Optional<PaymentModel>> {

    @Repository
    protected interface PaymentRepositoryMongoDB extends BaseRepository<PaymentEntity, String> {

        PaymentEntity findByExternalId(String id);
    }

    private final PaymentRepositoryMongoDB repository;
    private final IPaymentEntityMapper iPaymentMapper;

    @Override
    public Optional<PaymentModel> findById(String id) {
        var paymentEntity = repository.findByExternalId(id);

        if (Objects.nonNull(paymentEntity)) {
            return Optional.ofNullable(iPaymentMapper.toModel(paymentEntity));
        }

        return repository.findById(id).map(iPaymentMapper::toModel);
    }

}
