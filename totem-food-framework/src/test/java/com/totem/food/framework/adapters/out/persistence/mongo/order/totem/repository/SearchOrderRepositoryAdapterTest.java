package com.totem.food.framework.adapters.out.persistence.mongo.order.totem.repository;

import com.totem.food.application.ports.in.dtos.order.totem.OrderFilterDto;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.entity.OrderEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.mapper.IOrderEntityMapper;
import lombok.SneakyThrows;
import mocks.domains.OrderDomainMock;
import mocks.entity.OrderEntityMock;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchOrderRepositoryAdapterTest {

    @Spy
    private IOrderEntityMapper iOrderEntityMapper = Mappers.getMapper(IOrderEntityMapper.class);

    @Mock
    private SearchOrderRepositoryAdapter.OrderRepositoryMongoDB repository;

    private SearchOrderRepositoryAdapter searchOrderRepositoryAdapter;

    @Mock
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        searchOrderRepositoryAdapter = new SearchOrderRepositoryAdapter(repository, iOrderEntityMapper);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        autoCloseable.close();
    }


    @Test
    void findAllWhenNoFilterIsProvided() {

        //## Given - Mock
        var filter = OrderFilterDto.builder().build();

        //## When
        var result = searchOrderRepositoryAdapter.findAll(filter);

        //## Then
        assertThat(result).isEmpty();
        verify(repository, times(0)).findByFilter(any(ObjectId.class));
        verify(repository, times(0)).findById(any(String.class));
        verify(repository, times(0)).findByStatus(any(Set.class));
    }


    @Test
    void findAllWhenFilterByCustomerId() {

        // Mock - Object
        var filter = OrderFilterDto.builder().customerId("ad852cd2-fd7d-4377-b868-40508b58f384").build();
        var orderDomain = OrderDomainMock.getStatusNewMock();
        orderDomain.setId("0aa85a99-82bd-47b6-9f11-74b63f424d72");
        orderDomain.getCustomer().setId("ad852cd2-fd7d-4377-b868-40508b58f384");
        var orderEntity = OrderEntityMock.getMock();
        orderEntity.setId("0aa85a99-82bd-47b6-9f11-74b63f424d72");
        orderEntity.getCustomer().setId("ad852cd2-fd7d-4377-b868-40508b58f384");

        //## Given
        when(repository.findById(any()))
                .thenReturn(Optional.of(orderEntity));

        //## When
        var result = searchOrderRepositoryAdapter.findAll(filter);

        result.forEach(OrderDomain::updateOrderReceivedAt);

        //## Then
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(ZonedDateTime.class)
                .isEqualTo(List.of(orderDomain));
        verify(repository, times(1)).findById(filter.getCustomerId());
    }

    @Test
    void findAllWhenFilterByOrderId() {

        //## Mock - Object
        var filter = OrderFilterDto.builder().orderId("0aa85a99-82bd-47b6-9f11-74b63f424d72").build();
        var orderDomain = OrderDomainMock.getStatusNewMock();
        orderDomain.setId("0aa85a99-82bd-47b6-9f11-74b63f424d72");
        orderDomain.getCustomer().setId("ad852cd2-fd7d-4377-b868-40508b58f384");
        var orderEntity = OrderEntityMock.getMock();
        orderEntity.setId("0aa85a99-82bd-47b6-9f11-74b63f424d72");
        orderEntity.getCustomer().setId("ad852cd2-fd7d-4377-b868-40508b58f384");

        //## Given
        when(repository.findById(filter.getOrderId()))
                .thenReturn(Optional.of(orderEntity));

        //## When
        var result = searchOrderRepositoryAdapter.findAll(filter);

        result.forEach(OrderDomain::updateOrderReceivedAt);

        //## Then
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(ZonedDateTime.class)
                .isEqualTo(List.of(orderDomain));
        verify(repository, times(1)).findById(filter.getOrderId());
    }


    @Test
    @DisplayName("Should return a list of orders filtered by status")
    void findAllWhenFilterByStatus() {
        OrderFilterDto filter = OrderFilterDto.builder()
                .status(Set.of("NEW", "WAITING_PAYMENT"))
                .build();

        List<OrderEntity> orderEntities = List.of(
                OrderEntityMock.getMock());

        when(repository.findByStatus(filter.getStatus())).thenReturn(orderEntities);

        List<OrderDomain> result = searchOrderRepositoryAdapter.findAll(filter);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo("1");
        assertThat(result.get(0).getStatus()).isEqualTo(OrderStatusEnumDomain.NEW);
        assertThat(result.get(1).getId()).isEqualTo("2");
        assertThat(result.get(1).getStatus()).isEqualTo(OrderStatusEnumDomain.WAITING_PAYMENT);

        verify(repository, times(1)).findByStatus(filter.getStatus());
    }

    @Test
    void findAllWhenFilterCustomerId() {

        //## Mock - Object
        var filterCustomerId = OrderFilterDto.builder()
                .customerId("1")
                .build();
        var filterOrderId = OrderFilterDto.builder()
                .orderId("1")
                .build();
        var filterStatus = OrderFilterDto.builder()
                .status(Set.of(String.valueOf(OrderStatusEnumDomain.NEW)))
                .build();
        var orderDomain = OrderDomainMock.getStatusNewMock();
        var orderEntity = OrderEntityMock.getMock();

        //## Given
        when(repository.findByFilter(new ObjectId(filterCustomerId.getCustomerId())))
                .thenReturn(List.of(orderEntity));

        //## When
        var result = searchOrderRepositoryAdapter.findAll(filterCustomerId);

        //## Then
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(List.of(orderDomain));
        verify(iOrderEntityMapper, times(1)).toDomain(any(OrderEntity.class));

    }
}