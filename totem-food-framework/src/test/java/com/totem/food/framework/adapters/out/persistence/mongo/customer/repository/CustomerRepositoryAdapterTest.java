package com.totem.food.framework.adapters.out.persistence.mongo.customer.repository;

import com.totem.food.application.ports.in.dtos.customer.CustomerFilterDto;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.mapper.ICustomerEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static mocks.adapters.out.persistence.mongo.customer.entity.CustomerEntityMock.getMock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerRepositoryAdapterTest {

    private CustomerRepositoryAdapter customerRepositoryAdapter;
    @Spy
    private ICustomerEntityMapper iCustomerEntityMapper = Mappers.getMapper(ICustomerEntityMapper.class);

    @Mock
    private CustomerRepositoryAdapter.CustomerRepositoryMongoDB repository;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        customerRepositoryAdapter = new CustomerRepositoryAdapter(repository, iCustomerEntityMapper);
    }

    @Test
    void findAll() {

        //## Given
        final var customersEntity = List.of(getMock(), getMock());
        final var customerFilter = new CustomerFilterDto("Name");
        when(repository.findAll()).thenReturn(customersEntity);

        //## When
        var listCategoryDomain = customerRepositoryAdapter.findAll(customerFilter);

        //## Then
        assertThat(listCategoryDomain).usingRecursiveComparison().isEqualTo(customersEntity);
        verify(iCustomerEntityMapper, times(2)).toDomain(any(CustomerEntity.class));
    }

}