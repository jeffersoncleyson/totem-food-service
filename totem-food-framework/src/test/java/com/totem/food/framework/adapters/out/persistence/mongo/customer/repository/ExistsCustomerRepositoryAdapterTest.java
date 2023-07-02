package com.totem.food.framework.adapters.out.persistence.mongo.customer.repository;

import com.totem.food.domain.customer.CustomerDomain;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ExistsCustomerRepositoryAdapterTest {

    @Mock
    private ExistsCustomerRepositoryAdapter.CustomerRepositoryMongoDB repository;


    private ExistsCustomerRepositoryAdapter existsCustomerRepositoryAdapter;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        existsCustomerRepositoryAdapter = new ExistsCustomerRepositoryAdapter(repository);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void exists() {

        //### Given - Objects and Values
        final var name = "John";

        final var customerDomain = new CustomerDomain();
        customerDomain.setName(name);
        customerDomain.setCpf("12432385746");

        //### Given - Mocks
        when(repository.existsByCpfIgnoreCase(customerDomain.getCpf())).thenReturn(true);

        //### When
        final var result = existsCustomerRepositoryAdapter.exists(customerDomain);

        //### Then
        assertTrue(result);

    }
}