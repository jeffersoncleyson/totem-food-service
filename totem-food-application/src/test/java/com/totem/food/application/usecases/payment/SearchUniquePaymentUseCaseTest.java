package com.totem.food.application.usecases.payment;

import com.totem.food.application.ports.in.mappers.payment.IPaymentMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.domain.payment.PaymentDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
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
    private ISearchUniqueRepositoryPort<Optional<PaymentDomain>> iSearchUniqueRepositoryPort;

    @Spy
    private IPaymentMapper iPaymentMapper = Mappers.getMapper(IPaymentMapper.class);


    private SearchUniquePaymentUseCase searchUniquePaymentUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.searchUniquePaymentUseCase = new SearchUniquePaymentUseCase(iSearchUniqueRepositoryPort, iPaymentMapper);
    }

    @Test
    void item() {

        //## Given - Mock
        var paymentDomain = new PaymentDomain();
        paymentDomain.setId("1");
//        paymentDomain.setOrder(OrderDomainMock.getMock());
//        paymentDomain.setCustomer(CustomerDomainMock.getMock());
        paymentDomain.setPrice(49.99);
        paymentDomain.setToken("token");
        paymentDomain.setStatus(PaymentDomain.PaymentStatus.PENDING);
        paymentDomain.setModifiedAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"));
        paymentDomain.setCreateAt(ZonedDateTime.parse("2023-04-03T13:28:20.606-03:00"));


        //## Given
//        final var paymentDomain = PaymentDomainMock.getMock();
        when(iSearchUniqueRepositoryPort.findById(anyString())).thenReturn(Optional.of(paymentDomain));

        //## When
        final var paymentDto = searchUniquePaymentUseCase.item(anyString());

        //## Then
        assertNotNull(paymentDto);
        verify(iPaymentMapper, times(1)).toDto(any(PaymentDomain.class));
    }

}