package com.totem.food.framework.adapters.out.persistence.mongo.order.totem.repository;

import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.order.totem.OrderModel;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.entity.OrderEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.mapper.IOrderEntityMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchUniqueOrderRepositoryAdapterTest {

    @Mock
    private SearchUniqueOrderRepositoryAdapter.ProductRepositoryMongoDB repository;
    @Spy
    private IOrderEntityMapper iOrderEntityMapper = Mappers.getMapper(IOrderEntityMapper.class);

    private ISearchUniqueRepositoryPort<Optional<OrderModel>> iSearchUniqueRepositoryPort;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        iSearchUniqueRepositoryPort = new SearchUniqueOrderRepositoryAdapter(repository, iOrderEntityMapper);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void findById() {

        //## Given
        var id = UUID.randomUUID().toString();
        var orderEntity = OrderEntity.builder().id(id).build();

        //## When
        when(repository.findById(id)).thenReturn(Optional.of(orderEntity));

        var orderDomain = iSearchUniqueRepositoryPort.findById(id);

        //## Then
        assertTrue(orderDomain.isPresent());
        assertEquals(orderDomain.get().getId(), id);

    }
}