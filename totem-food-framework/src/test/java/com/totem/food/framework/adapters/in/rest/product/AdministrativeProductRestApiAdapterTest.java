package com.totem.food.framework.adapters.in.rest.product;


import com.totem.food.application.ports.in.dtos.product.ProductCreateDto;
import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.framework.test.utils.TestUtils;
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
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class AdministrativeProductRestApiAdapterTest {

    @Mock
    private ICreateUseCase<ProductCreateDto, ProductDto> createProductUseCase;

    private MockMvc mockMvc;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        final var administrativeProductRestApiAdapter = new AdministrativeProductRestApiAdapter(createProductUseCase);
        mockMvc = MockMvcBuilders.standaloneSetup(administrativeProductRestApiAdapter).build();
    }

    @AfterEach
    void after() throws Exception {
        autoCloseable.close();
    }

    @ParameterizedTest
    @ValueSource(strings = "/administrative/product")
    void createItem(String endpoint) throws Exception {

        //### Given - Objects and Values
        final var id = UUID.randomUUID().toString();
        final var name = "Coca-cola";
        final var description = "description";
        final var image = "https://mybucket.s3.amazonaws.com/myfolder/afile.jpg";
        final var price = 10D * (Math.random() + 1);
        final var category = "Refrigerante";
        final var modifiedAt = ZonedDateTime.now(ZoneOffset.UTC);
        final var createAt = ZonedDateTime.now(ZoneOffset.UTC);

        final var productDto = new ProductDto(
                id,
                name,
                description,
                image,
                price,
                category,
                modifiedAt,
                createAt
        );
        final var productCreateDto = new ProductCreateDto(
                name,
                description,
                image,
                price,
                category
        );

        //### Given - Mocks
        when(createProductUseCase.createItem(Mockito.any(ProductCreateDto.class))).thenReturn(productDto);

        final var jsonOpt = TestUtils.toJSON(productCreateDto);
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
        final var productDtoResponseOpt = TestUtils.toObject(responseJson, ProductDto.class);
        final var productDtoResponse = productDtoResponseOpt.orElseThrow();

        assertThat(productDto)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(ZonedDateTime.class)
                .isEqualTo(productDtoResponse);

    }

}