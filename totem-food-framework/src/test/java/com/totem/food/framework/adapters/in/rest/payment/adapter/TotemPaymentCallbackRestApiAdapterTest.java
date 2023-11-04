package com.totem.food.framework.adapters.in.rest.payment.adapter;

import com.totem.food.application.ports.in.dtos.payment.PaymentCallbackDto;
import com.totem.food.application.ports.in.dtos.payment.PaymentFilterDto;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import com.totem.food.framework.test.utils.TestUtils;
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

import java.util.UUID;

import static com.totem.food.framework.adapters.in.rest.constants.Routes.API_VERSION_1;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.TOTEM_PAYMENT_CALLBACK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class TotemPaymentCallbackRestApiAdapterTest {

    private final String POST_CALLBACK_PAYMENT = API_VERSION_1 + TOTEM_PAYMENT_CALLBACK;

    @Mock
    private IUpdateUseCase<PaymentFilterDto, Boolean> iUpdateUseCase;

    private MockMvc mockMvc;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        var totemPaymentCallbackRestApiAdapter = new TotemPaymentCallbackRestApiAdapter(iUpdateUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(totemPaymentCallbackRestApiAdapter).build();
    }

    @AfterEach
    void after() throws Exception {
        autoCloseable.close();
    }

    @ParameterizedTest
    @ValueSource(strings = POST_CALLBACK_PAYMENT)
    void testCallbackPaymentWhenUpdateSuccess(String endpoint) throws Exception {

        //## Mock - Objects
        var paymentCallbackDto = new PaymentCallbackDto();
        paymentCallbackDto.setPaymentId(UUID.randomUUID().toString());

        //### Given
        when(iUpdateUseCase.updateItem(any(PaymentFilterDto.class), any(String.class))).thenReturn(Boolean.TRUE);

        final var jsonOpt = TestUtils.toJSON(paymentCallbackDto);
        final var json = jsonOpt.orElseThrow();
        final var httpServletRequest = post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //### When
        final var resultActions = mockMvc.perform(httpServletRequest);

        //### Then
        resultActions.andDo(print())
                .andExpect(status().isOk());

    }

    @ParameterizedTest
    @ValueSource(strings = POST_CALLBACK_PAYMENT)
    void testCallbackPaymentWhenNoItemToUpdate(String endpoint) throws Exception {

        //### Given
        when(iUpdateUseCase.updateItem(any(), anyString())).thenReturn(Boolean.FALSE);

        //### When
        final var jsonOpt = TestUtils.toJSON(new PaymentCallbackDto());
        final var json = jsonOpt.orElseThrow();
        final var httpServletRequest = post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
        final var resultActions = mockMvc.perform(httpServletRequest);

        //### Then
        resultActions.andDo(print())
                .andExpect(status().isNoContent());

    }

}