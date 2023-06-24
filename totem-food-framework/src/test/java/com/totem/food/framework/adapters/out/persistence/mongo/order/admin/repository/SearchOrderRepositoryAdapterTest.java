package com.totem.food.framework.adapters.out.persistence.mongo.order.admin.repository;

import com.totem.food.application.ports.in.dtos.order.admin.OrderAdminFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.domain.order.admin.OrderAdminDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.admin.entity.OrderAdminEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.admin.mapper.IOrderAdminEntityMapper;
import org.apache.commons.collections4.CollectionUtils;
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

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchOrderRepositoryAdapterTest {

    @Mock
    private SearchOrderAdminRepositoryAdapter.OrderRepositoryMongoDB repository;
    @Spy
    private IOrderAdminEntityMapper iOrderEntityMapper = Mappers.getMapper(IOrderAdminEntityMapper.class);

    private ISearchRepositoryPort<OrderAdminFilterDto, List<OrderAdminDomain>> iSearchRepositoryPort;
    private AutoCloseable closeable;

    @BeforeEach
    private void beforeEach() {
        closeable = MockitoAnnotations.openMocks(this);
        iSearchRepositoryPort = new SearchOrderAdminRepositoryAdapter(repository, iOrderEntityMapper);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void findAll() {

        //### Given - Objects and Values
        final var customerId = UUID.randomUUID().toString();
        final var customerName = "Customer Name";
        final var customerCpf = "14354529689";
        final var customerEmail = "customer@email.com";
        final var customerMobile = "5535944345655";
        final var customerPassword= "%#AjOBF%w.<K";
        final var customerModifiedAt = ZonedDateTime.now(ZoneOffset.UTC).minusDays(10);
        final var customerCreateAt = ZonedDateTime.now(ZoneOffset.UTC).minusDays(10);
        final var customer = new CustomerEntity(
                customerId,
                customerName,
                customerCpf,
                customerEmail,
                customerMobile,
                customerPassword,
                customerModifiedAt,
                customerCreateAt
        );

        final var orderId = UUID.randomUUID().toString();
        final var price = new BigDecimal("59.90").doubleValue();
        final var createAt = ZonedDateTime.now(ZoneOffset.UTC);

        final var order = new OrderAdminEntity(
                orderId,
                price,
                customer,
                "NEW",
                createAt,
                null,
                null
        );
        final var orderAdminEntityList = List.of(order);
        final var orderFilterDto = OrderAdminFilterDto.builder().orderId(orderId).build();

        //### Given - Mocks
        when(repository.findAll()).thenReturn(orderAdminEntityList);


        //### When
        final var orderAdminDtoList = iSearchRepositoryPort.findAll(orderFilterDto);


        //### Then
        verify(iOrderEntityMapper, times(1)).toDomain(Mockito.any(OrderAdminEntity.class));
        verify(repository, times(1)).findAll();


        final var orderDomain = iOrderEntityMapper.toDomain(order);
        final var orderDomainList = List.of(orderDomain);

        assertTrue(CollectionUtils.isNotEmpty(orderAdminDtoList));
        assertThat(orderAdminDtoList)
                .usingRecursiveComparison()
                .isEqualTo(orderDomainList);
    }
}