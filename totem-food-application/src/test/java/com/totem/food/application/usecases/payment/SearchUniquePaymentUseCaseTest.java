package com.totem.food.application.usecases.payment;

import com.totem.food.application.ports.in.mappers.payment.IPaymentMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.payment.PaymentModel;
import lombok.SneakyThrows;
import mock.models.PaymentModelMock;
import org.junit.jupiter.api.AfterEach;
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


    @Mock
    private ISearchUniqueRepositoryPort<Optional<PaymentModel>> iSearchUniqueRepositoryPort;

    @Spy
    private IPaymentMapper iPaymentMapper = Mappers.getMapper(IPaymentMapper.class);


    private SearchUniquePaymentUseCase searchUniquePaymentUseCase;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        this.searchUniquePaymentUseCase = new SearchUniquePaymentUseCase(iSearchUniqueRepositoryPort, iPaymentMapper);
    }

    @SneakyThrows
    @AfterEach
    void down(){
        autoCloseable.close();
    }

    @Test
    void item() {

        //## Given - Mock
        var paymentDomain = PaymentModelMock.getPaymentStatusPendingMock();

        //## Given
        when(iSearchUniqueRepositoryPort.findById(anyString())).thenReturn(Optional.of(paymentDomain));

        //## When
        final var paymentDto = searchUniquePaymentUseCase.item(anyString());

        //## Then
        assertNotNull(paymentDto);
        verify(iPaymentMapper, times(1)).toDto(any(PaymentModel.class));
    }

}