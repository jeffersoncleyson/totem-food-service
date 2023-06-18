package com.totem.food.framework.adapters.out.persistence.mongo.product.repository;

import com.totem.food.application.ports.out.persistence.commons.IRemoveRepositoryPort;
import com.totem.food.domain.product.ProductDomain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DeleteProductRepositoryAdapterTest {

    @Mock
    private DeleteProductRepositoryAdapter.ProductRepositoryMongoDB repository;

    private IRemoveRepositoryPort<ProductDomain> iRemoveRepositoryPort;
    private AutoCloseable closeable;

    @BeforeEach
    private void beforeEach() {
        closeable = MockitoAnnotations.openMocks(this);
        iRemoveRepositoryPort = new DeleteProductRepositoryAdapter(repository);
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