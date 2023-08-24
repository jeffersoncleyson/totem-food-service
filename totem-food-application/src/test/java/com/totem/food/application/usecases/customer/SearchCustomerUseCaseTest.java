package com.totem.food.application.usecases.customer;

import com.totem.food.application.ports.in.dtos.customer.CustomerFilterDto;
import com.totem.food.application.ports.in.mappers.customer.ICustomerMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import lombok.SneakyThrows;
import mock.models.CustomerModelMock;
import mock.ports.in.dto.CustomerDtoMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchCustomerUseCaseTest {

    @Spy
    private ICustomerMapper iCustomerMapper = Mappers.getMapper(ICustomerMapper.class);

    @Mock
    private ISearchRepositoryPort<CustomerFilterDto, List<CustomerModel>> iCustomerRepositoryPort;

    private SearchCustomerUseCase searchCustomerUseCase;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
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
        var customerModel = CustomerModelMock.getMock();

        //## Given
        when(iCustomerRepositoryPort.findAll(any())).thenReturn(List.of(customerModel));

        //## When
        var listCustomerDto = searchCustomerUseCase.items(customerFilterDto);

        //## Then
        assertNotNull(listCustomerDto);
        assertEquals(listCustomerDto.get(0).getCpf(), customerDto.getCpf());
        verify(iCustomerMapper, times(1)).toDto(customerModel);
    }
}