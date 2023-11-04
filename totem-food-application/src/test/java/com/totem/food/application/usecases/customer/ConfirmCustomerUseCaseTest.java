package com.totem.food.application.usecases.customer;

import com.totem.food.application.ports.in.dtos.customer.CustomerConfirmDto;
import com.totem.food.application.ports.out.persistence.commons.IConfirmRepositoryPort;
import com.totem.food.application.usecases.commons.IConfirmUseCase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfirmCustomerUseCaseTest {

    @Spy
    private IConfirmUseCase<Boolean, CustomerConfirmDto> iConfirmUseCase;

    @Spy
    private IConfirmRepositoryPort<CustomerConfirmDto, Boolean> iConfirmRepositoryPort;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        iConfirmUseCase = new ConfirmCustomerUseCase(iConfirmRepositoryPort);
    }


    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void testConfirm() {

        //## Given
        when(iConfirmRepositoryPort.confirmItem(any())).thenReturn(Boolean.TRUE);

        //## When
        var result = iConfirmUseCase.confirm(any(CustomerConfirmDto.class));

        //## Then
        assertTrue(result);
    }
}