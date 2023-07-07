package com.totem.food.framework.adapters.out.persistence.mongo.customer.repository;

import com.totem.food.application.ports.out.persistence.commons.IRemoveRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.domain.customer.CustomerDomain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DeleteCustomerRepositoryAdapterTest {

    @Mock
    private DeleteCustomerRepositoryAdapter.CustomerRepositoryMongoDB repository;

    private IRemoveRepositoryPort<CustomerModel> iRemoveRepositoryPort;
    private AutoCloseable closeable;

    @BeforeEach
    private void beforeEach() {
        closeable = MockitoAnnotations.openMocks(this);
        iRemoveRepositoryPort = new DeleteCustomerRepositoryAdapter(repository);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void removeItem() {

        //### Given - Objects and Values
        final var id = UUID.randomUUID().toString();

        //### When
        iRemoveRepositoryPort.removeItem(id);

        //### Then
        verify(repository, times(1)).deleteById(Mockito.anyString());
    }
}