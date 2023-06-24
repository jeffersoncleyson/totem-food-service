package com.totem.food.framework.adapters.in.rest.combo;

import com.totem.food.application.ports.in.dtos.combo.ComboCreateDto;
import com.totem.food.application.ports.in.dtos.combo.ComboDto;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.framework.test.utils.TestUtils;
import lombok.SneakyThrows;
import mocks.dtos.ComboCreateDtoMock;
import mocks.dtos.ComboDtoMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class AdministrativeComboRestApiAdapterTest {

    @Mock
    private ICreateUseCase<ComboCreateDto, ComboDto> iCreateUseCase;

    private MockMvc mockMvc;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        final var administrativeComboRestApiAdapter = new AdministrativeComboRestApiAdapter(iCreateUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(administrativeComboRestApiAdapter).build();
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        autoCloseable.close();
    }

    @ParameterizedTest
    @ValueSource(strings = "/v1/administrative/combo")
    void createCombo(String endpoint) throws Exception {

        var comboCreateDto = ComboCreateDtoMock.getMock();
        var comboDto = ComboDtoMock.getMock();
        comboDto.setCreateAt(ZonedDateTime.now(ZoneOffset.UTC));
        comboDto.setModifiedAt(ZonedDateTime.now(ZoneOffset.UTC));

        //### Given
        when(iCreateUseCase.createItem(any(ComboCreateDto.class))).thenReturn(comboDto);

        final var jsonOpt = TestUtils.toJSON(comboCreateDto);
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
        final var comboDtoResponseOpt = TestUtils.toObject(responseJson, ComboDto.class);
        final var comboDtoResponse = comboDtoResponseOpt.orElseThrow();

        assertThat(comboDto)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(ZonedDateTime.class)
                .isEqualTo(comboDtoResponse);

        verify(iCreateUseCase, times(1)).createItem(Mockito.any(ComboCreateDto.class));

    }

}