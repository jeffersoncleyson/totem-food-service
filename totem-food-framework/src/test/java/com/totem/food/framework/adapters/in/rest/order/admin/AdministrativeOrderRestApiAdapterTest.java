package com.totem.food.framework.adapters.in.rest.order.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.in.dtos.order.admin.OrderAdminDto;
import com.totem.food.application.ports.in.dtos.order.admin.OrderAdminFilterDto;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.framework.test.utils.TestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class AdministrativeOrderRestApiAdapterTest {

    @Mock
    private ISearchUseCase<OrderAdminFilterDto, List<OrderAdminDto>> iSearchOrderUseCase;

    private MockMvc mockMvc;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        final var administrativeOrderRestApiAdapter = new AdministrativeOrderRestApiAdapter(iSearchOrderUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(administrativeOrderRestApiAdapter).build();
    }

    @AfterEach
    void after() throws Exception {
        autoCloseable.close();
    }

    @ParameterizedTest
    @ValueSource(strings = "/v1/administrative/orders")
    void listAll(String endpoint)  throws Exception {

        //### Given - Objects and Values
        final var customerId = UUID.randomUUID().toString();
        final var customerName = "Customer Name";
        final var customerCpf = "14354529689";
        final var customerEmail = "customer@email.com";
        final var customerMobile = "5535944345655";
        final var customerModifiedAt = ZonedDateTime.now(ZoneOffset.UTC).minusDays(10);
        final var customerCreateAt = ZonedDateTime.now(ZoneOffset.UTC).minusDays(10);
        final var customer = new CustomerDto(
                customerId,
                customerName,
                customerCpf,
                customerEmail,
                customerMobile,
                customerModifiedAt,
                customerCreateAt
        );

        final var orderId = UUID.randomUUID().toString();
        final var price = new BigDecimal("59.90").doubleValue();
        final var createAt = ZonedDateTime.now(ZoneOffset.UTC);

        final var order = new OrderAdminDto(
                orderId,
                price,
                customer,
                "NEW",
                createAt,
                null,
                null,
                0
        );

        final var orders = List.of(order);

        //### Given - Mocks
        when(iSearchOrderUseCase.items(Mockito.any(OrderAdminFilterDto.class))).thenReturn(orders);

        //### When
        final var httpServletRequest = get(endpoint)
                .contentType(MediaType.APPLICATION_JSON);
        final var resultActions = mockMvc.perform(httpServletRequest);

        //### Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        final var result = resultActions.andReturn();
        final var responseJson = result.getResponse().getContentAsString();
        final var ordersListDtoResponseOpt = TestUtils.toTypeReferenceObject(responseJson, new TypeReference<List<OrderAdminDto>>() {
        });
        final var ordersListDtoResponse = ordersListDtoResponseOpt.orElseThrow();

        assertTrue(CollectionUtils.isNotEmpty(ordersListDtoResponse));
        assertThat(ordersListDtoResponse)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(ZonedDateTime.class)
                .isEqualTo(orders);

        verify(iSearchOrderUseCase, times(1)).items(Mockito.any(OrderAdminFilterDto.class));
    }
}