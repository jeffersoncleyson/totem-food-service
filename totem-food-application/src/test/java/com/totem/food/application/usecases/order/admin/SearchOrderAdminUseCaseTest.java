package com.totem.food.application.usecases.order.admin;

import com.totem.food.application.ports.in.dtos.order.admin.OrderAdminDto;
import com.totem.food.application.ports.in.dtos.order.admin.OrderAdminFilterDto;
import com.totem.food.application.ports.in.dtos.order.totem.OrderDto;
import com.totem.food.application.ports.in.mappers.order.admin.IOrderAdminMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.order.admin.OrderAdminModel;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.domain.order.admin.OrderAdminDomain;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchOrderAdminUseCaseTest {

    @Spy
    private IOrderAdminMapper iOrderAdminMapper = Mappers.getMapper(IOrderAdminMapper.class);
    @Mock
    private ISearchRepositoryPort<OrderAdminFilterDto, List<OrderAdminModel>> iSearchOrderRepositoryPort;

    private ISearchUseCase<OrderAdminFilterDto, List<OrderAdminDto>> iSearchUseCase;
    private AutoCloseable closeable;

    @BeforeEach
    void beforeEach() {
        closeable = MockitoAnnotations.openMocks(this);
        iSearchUseCase = new SearchOrderAdminUseCase(iOrderAdminMapper, iSearchOrderRepositoryPort);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void items() {

        //### Given - Objects and Values
        final var customerId = UUID.randomUUID().toString();
        final var customerName = "Customer Name";
        final var customerCpf = "14354529689";
        final var customerEmail = "customer@email.com";
        final var customerMobile = "5535944345655";
        final var customerModifiedAt = ZonedDateTime.now(ZoneOffset.UTC).minusDays(10);
        final var customerCreateAt = ZonedDateTime.now(ZoneOffset.UTC).minusDays(10);
        final var customerDomain = new CustomerDomain();
        customerDomain.setId(customerId);
        customerDomain.setName(customerName);
        customerDomain.setCpf(customerCpf);
        customerDomain.setEmail(customerEmail);
        customerDomain.setMobile(customerMobile);
        customerDomain.setModifiedAt(customerModifiedAt);
        customerDomain.setCreateAt(customerCreateAt);


        final var orderId = UUID.randomUUID().toString();
        final var price = new BigDecimal("59.90").doubleValue();
        final var createAt = ZonedDateTime.now(ZoneOffset.UTC);

        final var order = new OrderAdminModel(
                orderId,
                price,
                customerDomain,
                "NEW",
                createAt,
                null,
                null,
                0
        );
        final var orderAdminDomainList = List.of(order);
        final var orderFilterDto = OrderAdminFilterDto.builder().build();

        //### Given - Mocks
        when(iSearchOrderRepositoryPort.findAll(Mockito.any(OrderAdminFilterDto.class))).thenReturn(orderAdminDomainList);

        //### When
        final var listAdminOrderDto = iSearchUseCase.items(orderFilterDto);

        //### Then
        verify(iOrderAdminMapper, times(1)).toDto(Mockito.any(OrderAdminDomain.class));
        verify(iSearchOrderRepositoryPort, times(1)).findAll(Mockito.any(OrderAdminFilterDto.class));

        final var orderDto = iOrderAdminMapper.toDto(order);
        final var oderDtoList = List.of(orderDto);

        assertThat(listAdminOrderDto)
                .usingRecursiveComparison()
                .isEqualTo(oderDtoList);
    }

    @Test
    void sortedOrderByStatusWhenSortedFlagIsTrue() {

        //## Mock - Object
        var filter = OrderAdminFilterDto.builder().onlyTreadmill(true).build();

        var order1 = createOrder("1", "NEW");
        var order2 = createOrder("2", "READY");
        var order3 = createOrder("3", "RECEIVED");
        var order4 = createOrder("4", "IN_PREPARATION");
        var order5 = createOrder("5", "FINALIZED");

        List<OrderDto> orderDtos = List.of(order1, order2, order3, order4, order5);

        //## Given
        when(iSearchOrderRepositoryPort.findAll(filter)).thenReturn(List.of(
                OrderAdminModel.builder().id("1").status(OrderStatusEnumDomain.NEW.key).createAt(ZonedDateTime.now()).build(),
                OrderAdminModel.builder().id("2").status(OrderStatusEnumDomain.READY.key).createAt(ZonedDateTime.now().plusMinutes(1)).build(),
                OrderAdminModel.builder().id("3").status(OrderStatusEnumDomain.RECEIVED.key).createAt(ZonedDateTime.now()).build(),
                OrderAdminModel.builder().id("4").status(OrderStatusEnumDomain.IN_PREPARATION.key).createAt(ZonedDateTime.now()).build(),
                OrderAdminModel.builder().id("5").status(OrderStatusEnumDomain.FINALIZED.key).createAt(ZonedDateTime.now().plusMinutes(3)).build()));

        //## When
        var result = iSearchUseCase.items(filter);

        //## Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(orderDtos.get(1).getStatus(), result.get(0).getStatus());

        verify(iSearchOrderRepositoryPort, times(1)).findAll(filter);
    }

    private OrderDto createOrder(String id, String status) {
        var order = new OrderDto();
        order.setId(id);
        order.setStatus(status);
        return order;
    }
}