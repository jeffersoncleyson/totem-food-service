package com.totem.food.application.usecases.customer;

import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.in.dtos.customer.CustomerFilterDto;
import com.totem.food.application.ports.in.mappers.customer.ICustomerMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.domain.customer.CustomerDomain;
import lombok.SneakyThrows;
import mock.domain.CustomerDomainMock;
import mock.ports.in.dto.CustomerDtoMock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Closeable;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchCustomerUseCaseTest {

    @Spy
    private ICustomerMapper iCustomerMapper = Mappers.getMapper(ICustomerMapper.class);

    @Mock
    private ISearchRepositoryPort<CustomerFilterDto, List<CustomerDomain>> iCustomerRepositoryPort;

    private SearchCustomerUseCase searchCustomerUseCase;

    @Mock
    private Closeable closeable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        searchCustomerUseCase = new SearchCustomerUseCase(iCustomerMapper, iCustomerRepositoryPort);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void items() {

        //## Mock - Object
        var customerFilterDto = new CustomerFilterDto("John");
        var customerDto = CustomerDtoMock.getMock();
        var customerDomain = CustomerDomainMock.getMock();

        //## Given
        when(iCustomerRepositoryPort.findAll(any())).thenReturn(List.of(customerDomain));

        //## When
        var listCustomerDto = searchCustomerUseCase.items(customerFilterDto);

        //## Then
        assertNotNull(listCustomerDto);
        assertEquals(listCustomerDto.get(0).getCpf(), customerDto.getCpf());
        verify(iCustomerMapper, times(1)).toDto(customerDomain);
    }
}