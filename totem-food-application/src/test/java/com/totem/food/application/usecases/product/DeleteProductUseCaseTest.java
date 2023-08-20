package com.totem.food.application.usecases.product;

import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.ports.out.persistence.commons.IRemoveRepositoryPort;
import com.totem.food.application.ports.out.persistence.product.ProductModel;
import com.totem.food.application.usecases.commons.IDeleteUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DeleteProductUseCaseTest {

    @Mock
    private IRemoveRepositoryPort<ProductModel> iSearchUniqueRepositoryPort;

    private IDeleteUseCase<String, ProductDto> iDeleteUseCase;

    private AutoCloseable closeable;

    @BeforeEach
    void beforeEach() {
        closeable = MockitoAnnotations.openMocks(this);
        this.iDeleteUseCase = new DeleteProductUseCase(iSearchUniqueRepositoryPort);
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
        iDeleteUseCase.removeItem(id);

        //### Then
        verify(iSearchUniqueRepositoryPort, times(1)).removeItem(Mockito.anyString());
    }
}