package com.totem.food.framework.adapters.out.persistence.mongo.payment.repository;

import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.payment.entity.PaymentEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.payment.mapper.IPaymentEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Component
public class CreatePaymentRepositoryAdapter implements ICreateRepositoryPort<PaymentModel> {


    @Repository
    protected interface PaymentRepositoryMongoDB extends BaseRepository<PaymentEntity, String> {

    }

    private final PaymentRepositoryMongoDB repository;
    private final IPaymentEntityMapper iPaymentMapper;

    @Override
    public PaymentModel saveItem(PaymentModel item) {
        final var entity = iPaymentMapper.toEntity(item);
        final var entitySaved = repository.save(entity);
        return iPaymentMapper.toModel(entitySaved);
    }

}
