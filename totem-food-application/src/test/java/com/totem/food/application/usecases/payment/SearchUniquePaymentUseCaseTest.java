package com.totem.food.application.usecases.payment;

import com.totem.food.application.ports.in.mappers.payment.IPaymentMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.domain.payment.PaymentDomain;
import mock.PaymentDomainMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchUniquePaymentUseCaseTest {

    @Spy
    private IPaymentMapper iPaymentMapper = Mappers.getMapper(IPaymentMapper.class);
    ;

    @Mock
    private ISearchUniqueRepositoryPort<Optional<PaymentDomain>> iSearchUniqueRepositoryPort;

    private SearchUniquePaymentUseCase searchUniquePaymentUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.searchUniquePaymentUseCase = new SearchUniquePaymentUseCase(iSearchUniqueRepositoryPort, iPaymentMapper);
    }

    @Test
    void item() {

        //## Given
        final var paymentDomain = PaymentDomainMock.getMock();
        when(iSearchUniqueRepositoryPort.findById(anyString())).thenReturn(Optional.of(paymentDomain));

        //## When
        final var paymentDto = searchUniquePaymentUseCase.item("1");

        //## Then
        assertNotNull(paymentDto);
        verify(iPaymentMapper, times(1)).toDto(any(PaymentDomain.class));
    }

}