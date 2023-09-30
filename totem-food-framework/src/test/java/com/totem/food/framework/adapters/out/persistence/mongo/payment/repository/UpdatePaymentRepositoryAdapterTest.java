package com.totem.food.framework.adapters.out.persistence.mongo.payment.repository;

import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
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
class UpdatePaymentRepositoryAdapterTest {

    @Mock
    private UpdatePaymentRepositoryAdapter.PaymentRepositoryMongoDB repository;
    @Spy
    private IPaymentEntityMapper iPaymentEntityMapper = Mappers.getMapper(IPaymentEntityMapper.class);

    private IUpdateRepositoryPort<PaymentModel> iUpdateRepositoryPort;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        iUpdateRepositoryPort = new UpdatePaymentRepositoryAdapter(repository, iPaymentEntityMapper);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        autoCloseable.close();
    }

    @Test
    void updateItem() {

        //## Given
        final var paymentDomain = PaymentModelMock.getPaymentDomain(PaymentDomain.PaymentStatus.PENDING);
        final var paymentEntity = PaymentEntityMock.getPaymentEntity(PaymentDomain.PaymentStatus.PENDING);

        //## Given Mocks
        when(repository.save(Mockito.any(PaymentEntity.class))).thenReturn(paymentEntity);

        //## When
        final var paymentDomainUpdated = iUpdateRepositoryPort.updateItem(paymentDomain);

        //## Then
        verify(iPaymentEntityMapper, times(1)).toEntity(Mockito.any(PaymentModel.class));
        verify(iPaymentEntityMapper, times(1)).toModel(Mockito.any(PaymentEntity.class));
        verify(repository, times(1)).save(Mockito.any(PaymentEntity.class));

        //Todo - Verificar mapeamento de cpf no target order
        final var paymentEntityUpdated = iPaymentEntityMapper.toEntity(paymentDomainUpdated);

        assertThat(paymentEntityUpdated).usingRecursiveComparison()
                .ignoringFields("order.cpf")
                .isEqualTo(paymentEntity);
    }
}