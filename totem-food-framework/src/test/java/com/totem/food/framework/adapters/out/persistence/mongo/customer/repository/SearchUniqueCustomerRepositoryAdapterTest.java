package com.totem.food.framework.adapters.out.persistence.mongo.customer.repository;

import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.mapper.ICustomerEntityMapper;
import lombok.SneakyThrows;
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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SearchUniqueCustomerRepositoryAdapterTest {

    @Mock
    private SearchUniqueCustomerRepositoryAdapter.CustomerRepositoryMongoDB repository;
    @Spy
    private ICustomerEntityMapper iCustomerEntityMapper = Mappers.getMapper(ICustomerEntityMapper.class);

    private ISearchUniqueRepositoryPort<Optional<CustomerModel>> iSearchUniqueRepositoryPort;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        iSearchUniqueRepositoryPort = new SearchUniqueCustomerRepositoryAdapter(repository, iCustomerEntityMapper);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }


    @Test
    void findById() {

        //## Given
        final var id = UUID.randomUUID().toString();

        var customerEntity = new CustomerEntity();
        customerEntity.setId(id);

        //## When
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(customerEntity));

        var customerDomain = iSearchUniqueRepositoryPort.findById(id);

        //## Then
        assertTrue(customerDomain.isPresent());
        assertEquals(customerDomain.get().getId(), id);
    }

    @Test
    void findByIdWhenEmpty() {

        //## Given
        var customerDomain = iSearchUniqueRepositoryPort.findById("");

        //## Then
        assertFalse(customerDomain.isPresent());
    }
}