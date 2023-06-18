package com.totem.food.framework.adapters.in.rest.product;


import com.fasterxml.jackson.core.type.TypeReference;
import com.totem.food.application.ports.in.dtos.product.ProductCreateDto;
import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.application.usecases.commons.ISearchUniqueUseCase;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @Mock
    private ISearchUseCase<ProductFilterDto, List<ProductDto>> iSearchProductUseCase;
    @Mock
    private ISearchUniqueUseCase<String, Optional<ProductDto>> iSearchUniqueUseCase;

    private MockMvc mockMvc;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        final var administrativeProductRestApiAdapter = new AdministrativeProductRestApiAdapter(createProductUseCase, iSearchProductUseCase, iSearchUniqueUseCase);
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

    @ParameterizedTest
    @ValueSource(strings = "/administrative/product")
    void listAll(String endpoint) throws Exception {

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
        final var productDtoList = List.of(productDto);

        final var filter = new ProductFilterDto(name);

        //### Given - Mocks
        when(iSearchProductUseCase.items(Mockito.any(ProductFilterDto.class))).thenReturn(productDtoList);

        final var httpServletRequest = get(endpoint)
                .queryParam("name", filter.getName());

        //### When
        final var resultActions = mockMvc.perform(httpServletRequest);

        //### Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        final var result = resultActions.andReturn();
        final var responseJson = result.getResponse().getContentAsString();
        final var productListDtoResponseOpt = TestUtils.toTypeReferenceObject(responseJson, new TypeReference<List<ProductDto>>() {
        });
        final var productListDtoResponse = productListDtoResponseOpt.orElseThrow();

        assertTrue(CollectionUtils.isNotEmpty(productListDtoResponse));
        assertThat(productListDtoResponse)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(ZonedDateTime.class)
                .isEqualTo(productDtoList);
    }

    @ParameterizedTest
    @ValueSource(strings = "/administrative/product/{productId}")
    void getById(String endpoint) throws Exception {

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

        //### Given - Mocks
        when(iSearchUniqueUseCase.item(Mockito.anyString())).thenReturn(Optional.of(productDto));

        final var httpServletRequest = get(endpoint, id);

        //### When
        final var resultActions = mockMvc.perform(httpServletRequest);

        //### Then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        final var result = resultActions.andReturn();
        final var responseJson = result.getResponse().getContentAsString();
        final var productDtoResponseOpt = TestUtils.toObject(responseJson, ProductDto.class);
        final var productDtoResponse = productDtoResponseOpt.orElseThrow();

        assertNotNull(productDtoResponse);
        assertThat(productDtoResponse)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(ZonedDateTime.class)
                .isEqualTo(productDto);
    }

}