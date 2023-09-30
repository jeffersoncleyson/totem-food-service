package com.totem.food.framework.adapters.out.persistence.mongo.payment.repository;

import com.totem.food.application.ports.in.dtos.payment.PaymentFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import com.totem.food.domain.payment.PaymentDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.payment.mapper.IPaymentEntityMapper;
import lombok.SneakyThrows;
import mocks.entity.PaymentEntityMock;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchPaymentRepositoryAdapterTest {

    @Mock
    private SearchPaymentRepositoryAdapter.PaymentRepositoryMongoDB repository;
    @Spy
    private IPaymentEntityMapper iPaymentMapper = Mappers.getMapper(IPaymentEntityMapper.class);

    private ISearchRepositoryPort<PaymentFilterDto, PaymentModel> iSearchRepositoryPort;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        iSearchRepositoryPort = new SearchPaymentRepositoryAdapter(repository, iPaymentMapper);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        autoCloseable.close();
    }

    @Test
    void findAllWithFilterOrderIdAndToken() {

        //## Given
        final var paymentEntity = PaymentEntityMock.getPaymentEntity(PaymentDomain.PaymentStatus.PENDING);
        final var paymentFilter = PaymentFilterDto.builder()
                .orderId(paymentEntity.getOrder().getId())
                .token(paymentEntity.getToken())
                .build();

        //## Given Mocks
        when(repository.findByFilter(Mockito.any(), Mockito.any())).thenReturn(paymentEntity);

        //## When
        final var paymentDomain = iSearchRepositoryPort.findAll(paymentFilter);

        //## Then
        verify(repository, times(1)).findByFilter(Mockito.any(), Mockito.any());
        verify(iPaymentMapper, times(1)).toModel(Mockito.any());

        final var paymentEntityConverted = iPaymentMapper.toEntity(paymentDomain);
        assertThat(paymentEntityConverted)
                .usingRecursiveComparison()
                .ignoringFields("order.cpf")
                .isEqualTo(paymentEntity);

    }

    @Test
    void findAllWithFilterOrderIdAndStatus() {

        //## Given
        final var paymentEntity = PaymentEntityMock.getPaymentEntity(PaymentDomain.PaymentStatus.PENDING);
        final var paymentFilter = PaymentFilterDto.builder()
                .orderId(paymentEntity.getOrder().getId())
                .status(OrderStatusEnumDomain.IN_PREPARATION.key)
                .build();

        //## Given Mocks
        when(repository.findPaymentByOrderAndStatus(Mockito.any(), Mockito.any())).thenReturn(paymentEntity);

        //## When
        final var paymentDomain = iSearchRepositoryPort.findAll(paymentFilter);

        //## Then
        verify(repository, times(1)).findPaymentByOrderAndStatus(Mockito.any(), Mockito.any());
        verify(iPaymentMapper, times(1)).toModel(Mockito.any());

        final var paymentEntityConverted = iPaymentMapper.toEntity(paymentDomain);
        assertThat(paymentEntityConverted)
                .usingRecursiveComparison()
                .ignoringFields("order.cpf")
                .isEqualTo(paymentEntity);

    }

    @Test
    void findAllNoResults() {

        //## Given
        final var paymentFilter = PaymentFilterDto.builder().build();

        //## When
        final var paymentDomain = iSearchRepositoryPort.findAll(paymentFilter);

        //## Then
        assertNull(paymentDomain);
        verify(repository, times(0)).findByFilter(Mockito.any(), Mockito.any());
        verify(repository, times(0)).findPaymentByOrderAndStatus(Mockito.any(), Mockito.any());
        verify(iPaymentMapper, times(0)).toModel(Mockito.any());

    }
}