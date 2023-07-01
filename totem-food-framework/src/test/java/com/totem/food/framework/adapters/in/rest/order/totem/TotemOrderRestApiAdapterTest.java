package com.totem.food.framework.adapters.in.rest.order.totem;

import com.totem.food.application.ports.in.dtos.combo.ComboDto;
import com.totem.food.application.ports.in.dtos.order.totem.*;
import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.application.usecases.commons.IUpdateStatusUseCase;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import com.totem.food.framework.test.utils.TestUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class TotemOrderRestApiAdapterTest {

    private final String ENDPOINT = "/v1/totem/order";

    private MockMvc mockMvc;

    private AutoCloseable autoCloseable;

    @Mock
    private ICreateUseCase<OrderCreateDto, OrderDto> iCreateUseCase;

    @Mock
    private ISearchUseCase<OrderFilterDto, List<OrderDto>> iSearchProductUseCase;

    @Mock
    private IUpdateUseCase<OrderUpdateDto, OrderDto> iUpdateUseCase;

    @Mock
    private IUpdateStatusUseCase<OrderDto> iUpdateStatusUseCase;

    private TotemOrderRestApiAdapter totemOrderRestApiAdapter;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        this.totemOrderRestApiAdapter = new TotemOrderRestApiAdapter(iCreateUseCase, iSearchProductUseCase, iUpdateUseCase, iUpdateStatusUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(totemOrderRestApiAdapter).build();
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        autoCloseable.close();
    }

    @ParameterizedTest
    @ValueSource(strings = ENDPOINT)
    void create(String endpoint) throws Exception {

        //## Mock - Object
        var orderCreateDto = new OrderCreateDto();
        orderCreateDto.setCustomerId("123");
        orderCreateDto.setCombos(List.of(new ItemQuantityDto(1, "combo")));
        orderCreateDto.setProducts(List.of(new ItemQuantityDto(1, "produto")));

        var comboDto = new ComboDto();
        comboDto.setName("Casa");
        comboDto.setPrice(BigDecimal.valueOf(25.0));

        var orderDto = new OrderDto();
        orderDto.setId("1");
        orderDto.setCustomerId("123");
        orderDto.setProducts(List.of(ProductDto.builder().id("1").build()));
        orderDto.setCombos(List.of(comboDto));
        orderDto.setStatus("NEW");
        orderDto.setPrice(25.0);

        //## Given
        when(iCreateUseCase.createItem(any(OrderCreateDto.class))).thenReturn(orderDto);

        final var jsonOpt = TestUtils.toJSON(orderCreateDto);
        final var json = jsonOpt.orElseThrow();
        final var httpServletRequest = post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //### When
        final var resultActions = mockMvc.perform(httpServletRequest);

        //### Then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        final var result = resultActions.andReturn();
        final var responseJson = result.getResponse().getContentAsString();
        final var orderDtoResponseOpt = TestUtils.toObject(responseJson, OrderDto.class);
        final var orderDtoResponse = orderDtoResponseOpt.orElseThrow();

        assertThat(orderDto)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(ZonedDateTime.class)
                .isNotNull();

        verify(iCreateUseCase, times(1)).createItem(any(OrderCreateDto.class));
    }


    @ParameterizedTest
    @ValueSource(strings = ENDPOINT)
    void listAll(String endpoint) throws Exception {

        //## Mock - Object
        var comboDto = new ComboDto();
        comboDto.setName("Casa");
        comboDto.setPrice(BigDecimal.valueOf(25.0));

        var orderDto = new OrderDto();
        orderDto.setId("1");
        orderDto.setCustomerId("123");
        orderDto.setProducts(List.of(ProductDto.builder().id("1").build()));
        orderDto.setCombos(List.of(comboDto));
        orderDto.setStatus("NEW");
        orderDto.setPrice(25.0);

        var orderFilterDto = OrderFilterDto.builder()
                .customerId("123")
                .orderId("1")
                .status(Set.of("NEW"))
                .build();

        //## Given
        when(iSearchProductUseCase.items(any(OrderFilterDto.class))).thenReturn(Collections.singletonList(orderDto));

        final var httpServletRequest = get(endpoint)
                .queryParam("customerId", orderFilterDto.getCustomerId());

        //### When
        final var resultActions = mockMvc.perform(httpServletRequest);

        //### Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        final var result = resultActions.andReturn();
        final var responseJson = result.getResponse().getContentAsString();
        final var orderDtoResponseOpt = TestUtils.toObject(responseJson, OrderDto.class);
        final var orderDtoResponse = orderDtoResponseOpt.orElseThrow();

        assertThat(orderDto)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(ZonedDateTime.class)
                .isNotNull();

        verify(iSearchProductUseCase, times(1)).items(any(OrderFilterDto.class));
    }

    @Test
    void update() {
    }

}