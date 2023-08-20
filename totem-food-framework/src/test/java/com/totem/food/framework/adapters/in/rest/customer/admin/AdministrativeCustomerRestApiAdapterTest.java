package com.totem.food.framework.adapters.in.rest.customer.admin;

import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.in.dtos.customer.CustomerFilterDto;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import lombok.SneakyThrows;
import mocks.dtos.CustomerDtoMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdministrativeCustomerRestApiAdapterTest {

    @Mock
    private ISearchUseCase<CustomerFilterDto, List<CustomerDto>> iSearchCustomerUseCase;

    private AdministrativeCustomerRestApiAdapter administrativeCustomerRestApiAdapter;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        administrativeCustomerRestApiAdapter = new AdministrativeCustomerRestApiAdapter(iSearchCustomerUseCase);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        autoCloseable.close();
    }

    @Test
    void listAllWhenNoCustomersMatchTheFilter() {
        //## Given - Mocks
        var customerFilterDto = new CustomerFilterDto("John");

        when(iSearchCustomerUseCase.items(customerFilterDto)).thenReturn(List.of());

        //## When
        ResponseEntity<List<CustomerDto>> response = administrativeCustomerRestApiAdapter.listAll(customerFilterDto);

        //## Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(iSearchCustomerUseCase, times(1)).items(customerFilterDto);
    }

    @Test
    void listAllWhenFilterIsProvided() {

        //## Given - Mocks
        var customerDto = CustomerDtoMock.getMock();
        var customerFilterDto = new CustomerFilterDto("John");
        var customersDto = List.of(customerDto);

        when(iSearchCustomerUseCase.items(customerFilterDto)).thenReturn(customersDto);

        //## When
        List<CustomerDto> result = administrativeCustomerRestApiAdapter.listAll(customerFilterDto).getBody();

        //## Then
        assertNotNull(result);
        assertEquals(customersDto, result);

        verify(iSearchCustomerUseCase, times(1)).items(customerFilterDto);
    }

    @Test
    void listAllWhenNoFilterIsProvided() {

        //## Given - Mocks
        var customerDto = CustomerDtoMock.getMock();
        var customersDto = List.of(customerDto);

        when(iSearchCustomerUseCase.items(null)).thenReturn(customersDto);

        //## When
        ResponseEntity<List<CustomerDto>> responseEntity = administrativeCustomerRestApiAdapter.listAll(null);

        //## Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(customersDto, responseEntity.getBody());

        verify(iSearchCustomerUseCase, times(1)).items(null);
    }

}