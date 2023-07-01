package com.totem.food.framework.adapters.out.persistence.mongo.order.totem.repository;

import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.entity.OrderEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.mapper.IOrderEntityMapper;
import lombok.SneakyThrows;
import mocks.domains.OrderDomainMock;
import mocks.entity.OrderEntityMock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOrderRepositoryAdapterTest {

    @Spy
    private IOrderEntityMapper iOrderEntityMapper = Mappers.getMapper(IOrderEntityMapper.class);

    @Mock
    private CreateOrderRepositoryAdapter.OrderRepositoryMongoDB repository;

    private CreateOrderRepositoryAdapter createOrderRepositoryAdapter;

    @Mock
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createOrderRepositoryAdapter = new CreateOrderRepositoryAdapter(repository, iOrderEntityMapper);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        autoCloseable.close();
    }

    @Test
    void saveItem() {

        //## Mock - Object
        var orderEntity = OrderEntityMock.getMock();
        orderEntity.setId("745e557e-fe6d-4c6d-8ef4-44222c92612b");
        orderEntity.getCustomer().setId("edc49fd2-a59d-402b-b1b3-fc05d9e1287d");
        var orderDomain = OrderDomainMock.getStatusNewMock();
        orderDomain.getCustomer().setId("edc49fd2-a59d-402b-b1b3-fc05d9e1287d");
        orderDomain.setId("745e557e-fe6d-4c6d-8ef4-44222c92612b");

        //## Given
        when(repository.save(any(OrderEntity.class))).thenReturn(orderEntity);

        //## When
        var result = createOrderRepositoryAdapter.saveItem(orderDomain);

        result.updateOrderReceivedAt();

        //## Then
        Assertions.assertThat(result).usingRecursiveComparison()
                .ignoringFieldsOfTypes(ZonedDateTime.class)
                .isEqualTo(orderDomain);
        verify(iOrderEntityMapper, times(1)).toEntity(any(OrderDomain.class));
        verify(iOrderEntityMapper, times(1)).toDomain(any(OrderEntity.class));
    }
}