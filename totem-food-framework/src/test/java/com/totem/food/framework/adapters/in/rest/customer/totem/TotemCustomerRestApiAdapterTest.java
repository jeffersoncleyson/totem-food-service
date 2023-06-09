package com.totem.food.framework.adapters.in.rest.customer.totem;

import com.totem.food.application.ports.in.dtos.customer.CustomerCreateDto;
import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.application.usecases.commons.IDeleteUseCase;
import com.totem.food.framework.test.utils.TestUtils;
import lombok.SneakyThrows;
import org.bson.types.ObjectId;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private MockMvc mockMvc;

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        final var totemCustomerRestApiAdapter = new TotemCustomerRestApiAdapter(createCustomerUseCase, iDeleteUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(totemCustomerRestApiAdapter).build();
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @ParameterizedTest
    @ValueSource(strings = "/v1/totem/customer")
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
    @ValueSource(strings = "/v1/totem/customer/{customerId}")
    void deleteCustomer(String endpoint) throws Exception {

        //## Given
        final var id = new ObjectId().toHexString();

        //### Given - Mocks
        when(iDeleteUseCase.removeItem(Mockito.anyString())).thenReturn(null);

        final var httpServletRequest = delete(endpoint, id);

        //### When
        final var resultActions = mockMvc.perform(httpServletRequest);

        //### Then
        resultActions.andDo(print())
                .andExpect(status().isNoContent());

        verify(iDeleteUseCase, times(1)).removeItem(Mockito.anyString());
    }
}