package com.totem.food.framework.adapters.out.persistence.mongo.payment.repository;

import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.domain.payment.PaymentDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.payment.entity.PaymentEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.payment.mapper.IPaymentEntityMapper;
import lombok.SneakyThrows;
import mocks.entity.PaymentEntityMock;
import mocks.models.PaymentModelMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePaymentRepositoryAdapterTest {

    @Mock
    private CreatePaymentRepositoryAdapter.PaymentRepositoryMongoDB repository;
    @Spy
    private IPaymentEntityMapper iPaymentMapper = Mappers.getMapper(IPaymentEntityMapper.class);

    private ICreateRepositoryPort<PaymentModel> iCreateRepositoryPort;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        iCreateRepositoryPort = new CreatePaymentRepositoryAdapter(repository, iPaymentMapper);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        autoCloseable.close();
    }


    @Test
    void saveItem() {

        //## Given
        final var paymentModel = PaymentModelMock.getPaymentDomain(PaymentDomain.PaymentStatus.PENDING);
        final var paymentEntity = PaymentEntityMock.getPaymentEntity(PaymentDomain.PaymentStatus.PENDING);

        //## Given Mocks
        when(repository.save(Mockito.any(PaymentEntity.class))).thenReturn(paymentEntity);

        //## When
        final var paymentDomainUpdated = iCreateRepositoryPort.saveItem(paymentModel);

        //## Then
        verify(iPaymentMapper, times(1)).toEntity(Mockito.any(PaymentModel.class));
        verify(iPaymentMapper, times(1)).toModel(Mockito.any(PaymentEntity.class));
        verify(repository, times(1)).save(Mockito.any(PaymentEntity.class));

        final var paymentEntityUpdated = iPaymentMapper.toEntity(paymentDomainUpdated);
        assertThat(paymentEntityUpdated).usingRecursiveComparison()
                .ignoringFields("order.cpf")
                .isEqualTo(paymentEntity);
    }
}