package com.totem.food.application.usecases.order;

import com.totem.food.application.ports.in.dtos.order.OrderAdminDto;
import com.totem.food.application.ports.in.dtos.order.OrderFilterDto;
import com.totem.food.application.ports.in.mappers.order.IOrderAdminMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.domain.order.OrderAdminDomain;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchOrderAdminUseCaseTest {

    @Spy
    private IOrderAdminMapper iOrderAdminMapper = Mappers.getMapper(IOrderAdminMapper.class);
    @Mock
    private ISearchRepositoryPort<OrderFilterDto, List<OrderAdminDomain>> iSearchOrderRepositoryPort;

    private ISearchUseCase<OrderFilterDto, List<OrderAdminDto>> iSearchUseCase;
    private AutoCloseable closeable;

    @BeforeEach
    private void beforeEach() {
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
        final var customer = new CustomerDomain(
                customerId,
                customerName,
                customerCpf,
                customerEmail,
                customerMobile,
                customerModifiedAt,
                customerCreateAt
        );

        final var orderId = UUID.randomUUID().toString();
        final var showNumber = 12;
        final var amount = new BigDecimal("59.90");
        final var createAt = ZonedDateTime.now(ZoneOffset.UTC);

        final var order = new OrderAdminDomain(
                orderId,
                showNumber,
                amount,
                customer,
                createAt
        );
        final var orderAdminDomainList = List.of(order);
        final var orderFilterDto = OrderFilterDto.builder().orderId(orderId).build();

        //### Given - Mocks
        when(iSearchOrderRepositoryPort.findAll(Mockito.any(OrderFilterDto.class))).thenReturn(orderAdminDomainList);

        //### When
        final var listAdminOrderDto = iSearchUseCase.items(orderFilterDto);

        //### Then
        verify(iOrderAdminMapper, times(1)).toDto(Mockito.any(OrderAdminDomain.class));
        verify(iSearchOrderRepositoryPort, times(1)).findAll(Mockito.any(OrderFilterDto.class));

        final var orderDto = iOrderAdminMapper.toDto(order);
        final var oderDtoList = List.of(orderDto);

        assertThat(listAdminOrderDto)
                .usingRecursiveComparison()
                .isEqualTo(oderDtoList);


    }
}