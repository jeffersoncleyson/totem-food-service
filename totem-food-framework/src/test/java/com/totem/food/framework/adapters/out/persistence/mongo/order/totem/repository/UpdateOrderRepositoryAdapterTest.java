package com.totem.food.framework.adapters.out.persistence.mongo.order.totem.repository;

import com.totem.food.application.ports.out.persistence.order.totem.OrderModel;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.entity.OrderEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.mapper.IOrderEntityMapper;
import lombok.SneakyThrows;
import mocks.entity.OrderEntityMock;
import mocks.models.OrderModelMock;
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

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateOrderRepositoryAdapterTest {

    @Mock
    private UpdateOrderRepositoryAdapter.ProductRepositoryMongoDB repository;

    @Spy
    private IOrderEntityMapper iOrderEntityMapper = Mappers.getMapper(IOrderEntityMapper.class);

    private UpdateOrderRepositoryAdapter updateOrderRepositoryAdapter;

    @Mock
    private AutoCloseable autoCloseable;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        updateOrderRepositoryAdapter = new UpdateOrderRepositoryAdapter(repository, iOrderEntityMapper);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        autoCloseable.close();
    }

    @Test
    void updateItem() {

        //### Given - Objects and Values
        final var id = UUID.randomUUID().toString();
        var orderEntity = OrderEntityMock.getMock();
        var orderDomain = OrderModelMock.getOrderModel(OrderStatusEnumDomain.NEW);

        //### Given
        when(repository.save(Mockito.any(OrderEntity.class))).thenReturn(orderEntity);

        //### When
        final var orderDomainSaved = updateOrderRepositoryAdapter.updateItem(orderDomain);

        //### Then
        verify(iOrderEntityMapper, times(1)).toEntity(Mockito.any(OrderModel.class));
        verify(repository, times(1)).save(Mockito.any(OrderEntity.class));
        verify(iOrderEntityMapper, times(1)).toModel(Mockito.any(OrderEntity.class));

        assertThat(orderDomain)
                .usingRecursiveComparison()
                .ignoringFields("customer.id", "id")
                .ignoringFieldsOfTypes(ZonedDateTime.class)
                .isEqualTo(orderDomainSaved);

        assertNotNull(orderDomainSaved.getId());
    }
}