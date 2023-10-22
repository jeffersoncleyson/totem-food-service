package com.totem.food.framework.adapters.in.rest.customer.totem;

import com.totem.food.application.ports.in.dtos.customer.CustomerConfirmDto;
import com.totem.food.application.ports.in.dtos.customer.CustomerCreateDto;
import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.usecases.commons.IConfirmUseCase;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.application.usecases.commons.IDeleteUseCase;
import com.totem.food.framework.test.utils.TestUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.cloud.openfeign.security.OAuth2AccessTokenInterceptor;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import static com.totem.food.framework.adapters.in.rest.constants.Routes.API_VERSION_1;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.CONFIRM_CUSTOMER;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.CUSTOMER_ID;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.TOTEM_CUSTOMER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class TotemCustomerRestApiAdapterTest {

    @Mock
    private ICreateUseCase<CustomerCreateDto, CustomerDto> createCustomerUseCase;
    @Mock
    private IDeleteUseCase<String, CustomerDto> iDeleteUseCase;
    @Mock
    private IConfirmUseCase<Boolean, CustomerConfirmDto> iConfirmUseCase;

    private MockMvc mockMvc;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        final var totemCustomerRestApiAdapter = new TotemCustomerRestApiAdapter(createCustomerUseCase, iDeleteUseCase, iConfirmUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(totemCustomerRestApiAdapter).build();
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @ParameterizedTest
    @ValueSource(strings = API_VERSION_1 + TOTEM_CUSTOMER)
    void createCustomer(String endpoint) throws Exception {

        //### Given - Objects and Values
        final var name = "John";
        final var email = "john@gmail.com";
        final var cpf = "51397955074";
        final var mobile = "5511900112233";
        final var password = "%#AjOBF%w.<K";
        final var modifiedAt = ZonedDateTime.now(ZoneOffset.UTC);
        final var createAt = ZonedDateTime.now(ZoneOffset.UTC);


        final var customerDto = new CustomerDto();
        customerDto.setName(name);
        customerDto.setEmail(email);
        customerDto.setCpf(cpf);
        customerDto.setMobile(mobile);
        customerDto.setModifiedAt(modifiedAt);
        customerDto.setCreateAt(createAt);

        final var customerCreateDto = new CustomerCreateDto();
        customerCreateDto.setName(name);
        customerCreateDto.setEmail(email);
        customerCreateDto.setCpf(cpf);
        customerCreateDto.setMobile(mobile);
        customerCreateDto.setPassword(password);

        //### Given - Mocks
        when(createCustomerUseCase.createItem(Mockito.any(CustomerCreateDto.class))).thenReturn(customerDto);

        final var jsonOpt = TestUtils.toJSON(customerCreateDto);
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
        final var customerDtoResponseOpt = TestUtils.toObject(responseJson, CustomerDto.class);
        final var customerDtoResponse = customerDtoResponseOpt.orElseThrow();

        assertThat(customerDto)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(ZonedDateTime.class)
                .isEqualTo(customerDtoResponse);

        verify(createCustomerUseCase, times(1)).createItem(Mockito.any(CustomerCreateDto.class));

    }

    @ParameterizedTest
    @ValueSource(strings = API_VERSION_1 + TOTEM_CUSTOMER)
    void deleteCustomer(String endpoint) throws Exception {

        //### Given - Mocks
        when(iDeleteUseCase.removeItem(Mockito.anyString())).thenReturn(null);

        final var httpServletRequest = delete(endpoint)
                .header(
                        OAuth2AccessTokenInterceptor.AUTHORIZATION,
                        OAuth2AccessTokenInterceptor.BEARER.concat(" access_token")
                );

        //### When
        final var resultActions = mockMvc.perform(httpServletRequest);

        //### Then
        resultActions.andDo(print())
                .andExpect(status().isNoContent());

        verify(iDeleteUseCase, times(1)).removeItem(Mockito.anyString());
    }

    @ParameterizedTest
    @ValueSource(strings = API_VERSION_1 + TOTEM_CUSTOMER + CUSTOMER_ID + CONFIRM_CUSTOMER)
    void confirm(String endpoint) throws Exception {

        //### Given - Objects and Values
        final var cpf = "51397955074";

        var customerConfirmDto = new CustomerConfirmDto();
        customerConfirmDto.setCpf(cpf);
        customerConfirmDto.setCode(UUID.randomUUID().toString());

        //### Given - Mocks
        final var jsonOpt = TestUtils.toJSON(customerConfirmDto);
        final var json = jsonOpt.orElseThrow();
        final var httpServletRequest = put(endpoint, customerConfirmDto.getCpf(), customerConfirmDto.getCode())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //### When
        final var resultActions = mockMvc.perform(httpServletRequest);

        //### Then
        resultActions.andDo(print())
                .andExpect(status().isNoContent());

        verify(iConfirmUseCase, times(1)).confirm(any(CustomerConfirmDto.class));

    }
}