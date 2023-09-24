package com.totem.food.framework.adapters.out.persistence.mongo.order.totem.repository;

import com.totem.food.application.ports.in.dtos.order.totem.OrderFilterDto;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.mapper.IOrderEntityMapper;
import lombok.SneakyThrows;
import mocks.domains.OrderDomainMock;
import mocks.entity.OrderEntityMock;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
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

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
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
        verify(repository, never()).findByFilter(any(String.class));
        verify(repository, never()).findById(any(String.class));
        verify(repository, never()).findByStatus(any(Set.class));
    }

    @Test
    void findAllWhenFilterByCustomerId() {

        //## Mock - Object
        var hexString = new ObjectId().toHexString();
        var filter = OrderFilterDto.builder()
                .cpf(hexString)
                .build();
        var orderDomain = OrderDomainMock.getStatusNewMock();
        orderDomain.setId("0aa85a99-82bd-47b6-9f11-74b63f424d72");
        orderDomain.getCustomer().setId("ad852cd2-fd7d-4377-b868-40508b58f384");
        var orderEntity = OrderEntityMock.getMock();
        orderEntity.setId("0aa85a99-82bd-47b6-9f11-74b63f424d72");
        orderEntity.setCpf("ad852cd2-fd7d-4377-b868-40508b58f384");

        //## Given
        when(repository.findByFilter(any(String.class))).thenReturn(List.of(orderEntity));

        //## When
        var result = searchOrderRepositoryAdapter.findAll(filter);

        result.forEach(orderModel -> orderModel.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC)));

        //## Then
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(ZonedDateTime.class)
                .isEqualTo(List.of(orderDomain));
        verify(repository, times(1)).findByFilter(any());
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
        orderEntity.setCpf("ad852cd2-fd7d-4377-b868-40508b58f384");

        //## Given
        when(repository.findById(filter.getOrderId()))
                .thenReturn(Optional.of(orderEntity));

        //## When
        var result = searchOrderRepositoryAdapter.findAll(filter);

        result.forEach(orderModel -> orderModel.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC)));

        //## Then
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(ZonedDateTime.class)
                .isEqualTo(List.of(orderDomain));
        verify(repository, times(1)).findById(filter.getOrderId());
    }


    @Test
    void findAllWhenFilterByStatus() {

        //## Mock - Object
        var filter = OrderFilterDto.builder()
                .status(Set.of("NEW", "WAITING_PAYMENT"))
                .build();
        var orderDomain = OrderDomainMock.getStatusNewMock();
        orderDomain.setId("0aa85a99-82bd-47b6-9f11-74b63f424d72");
        orderDomain.getCustomer().setId("ad852cd2-fd7d-4377-b868-40508b58f384");
        var orderEntity = OrderEntityMock.getMock();
        orderEntity.setId("0aa85a99-82bd-47b6-9f11-74b63f424d72");
        orderEntity.setCpf("ad852cd2-fd7d-4377-b868-40508b58f384");

        //## Given
        when(repository.findByStatus(filter.getStatus())).thenReturn(List.of(orderEntity));

        //## When
        var result = searchOrderRepositoryAdapter.findAll(filter);

        //## Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(orderDomain.getId());
        assertThat(result.get(0).getStatus()).isEqualTo(orderDomain.getStatus());

        verify(repository, times(1)).findByStatus(filter.getStatus());
    }

}