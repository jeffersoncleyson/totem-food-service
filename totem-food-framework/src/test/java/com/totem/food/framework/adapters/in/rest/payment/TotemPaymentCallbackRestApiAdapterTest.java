package com.totem.food.framework.adapters.in.rest.payment;

import com.totem.food.application.ports.in.dtos.payment.PaymentFilterDto;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import com.totem.food.framework.adapters.in.rest.payment.adapter.TotemPaymentCallbackRestApiAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import java.util.UUID;

import static com.totem.food.framework.adapters.in.rest.constants.Routes.API_VERSION_1;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.PAYMENT_ORDER_ID;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.TOTEM_PAYMENT_CALLBACK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Disabled
class TotemPaymentCallbackRestApiAdapterTest {

    @Mock
    private final IUpdateUseCase<PaymentFilterDto, Boolean> iUpdateUseCase;

    private MockMvc mockMvc;
    private AutoCloseable autoCloseable;

    TotemPaymentCallbackRestApiAdapterTest(IUpdateUseCase<PaymentFilterDto, Boolean> iUpdateUseCase) {
        this.iUpdateUseCase = iUpdateUseCase;
    }

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        final var totemPaymentCallbackRestApiAdapter = new TotemPaymentCallbackRestApiAdapter(iUpdateUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(totemPaymentCallbackRestApiAdapter).build();
    }

    @AfterEach
    void after() throws Exception {
        autoCloseable.close();
    }

    @ParameterizedTest
    @ValueSource(strings = API_VERSION_1 + TOTEM_PAYMENT_CALLBACK + PAYMENT_ORDER_ID)
    void getItem(String endpoint) throws Exception {

        //### Given - Mocks
        when(iUpdateUseCase.updateItem(any(), anyString())).thenReturn(true);

        //### When
        final var httpServletRequest = put(endpoint, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .header("token", UUID.randomUUID().toString());
        final var resultActions = mockMvc.perform(httpServletRequest);

        //### Then
        resultActions.andDo(print())
                .andExpect(status().isNoContent());

    }

    @ParameterizedTest
    @ValueSource(strings = API_VERSION_1 + TOTEM_PAYMENT_CALLBACK + PAYMENT_ORDER_ID)
    void getItemNotProcessed(String endpoint) throws Exception {

        //### Given - Mocks
        when(iUpdateUseCase.updateItem(any(), anyString())).thenReturn(false);

        //### When
        final var httpServletRequest = put(endpoint, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .header("token", UUID.randomUUID().toString());
        final var resultActions = mockMvc.perform(httpServletRequest);

        //### Then
        resultActions.andDo(print())
                .andExpect(status().isBadRequest());

    }
}