package com.totem.food.framework.adapters.in.rest.customer.totem;

import com.totem.food.application.ports.in.dtos.customer.CustomerDto;
import com.totem.food.application.usecases.commons.ILoginUseCase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class TotemLoginRestApiAdapterTest {

    @Mock
    private ILoginUseCase<CustomerDto> iLoginUseCase;

    private MockMvc mockMvc;
    private AutoCloseable autoCloseable;


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        final var totemLoginRestApiAdapter = new TotemLoginRestApiAdapter(iLoginUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(totemLoginRestApiAdapter).build();
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        autoCloseable.close();
    }

    @ParameterizedTest
    @ValueSource(strings = "/v1/totem/login")
    void findById(String endpoint) throws Exception {

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

        //### Given - Mocks
        when(iLoginUseCase.login(cpf, password)).thenReturn(customerDto);


        final var httpServletRequest = get(endpoint)
                .header("cpf", cpf)
                .header("password", password)
                .accept(MediaType.APPLICATION_JSON);

        //### When
        final var resultActions = mockMvc.perform(httpServletRequest);

        //### Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        final var result = resultActions.andReturn();

        assertThat(result.getResponse())
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(ZonedDateTime.class)
                .isNotNull();

        verify(iLoginUseCase, times(1)).login(cpf, password);

    }
}