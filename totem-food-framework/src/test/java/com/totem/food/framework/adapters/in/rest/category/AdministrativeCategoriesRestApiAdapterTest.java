package com.totem.food.framework.adapters.in.rest.category;

import com.totem.food.application.ports.in.dtos.category.CategoryCreateDto;
import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.application.usecases.commons.IDeleteUseCase;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdministrativeCategoriesRestApiAdapterTest {

    @Mock
    private ICreateUseCase<CategoryCreateDto, CategoryDto> iCreateCategoryUseCase;

    @Mock
    private ISearchUseCase<String, CategoryDto> iSearchCategoryUseCase;

    @Mock
    private IDeleteUseCase iDeleteCategoryUseCase;

    @Mock
    private IUpdateUseCase<CategoryCreateDto, CategoryDto> iUpdateCategoryUseCase;

    @InjectMocks
    private AdministrativeCategoriesRestApiAdapter administrativeCategoriesRestApiAdapter;

    @Test
    void createCategory() {

        //## When
        var categoryCreateDto = new CategoryCreateDto("Suco");
        var categoryDto = new CategoryDto("1", "Suco", ZonedDateTime.now(), ZonedDateTime.now());
        when(iCreateCategoryUseCase.createItem(categoryCreateDto)).thenReturn(categoryDto);

        //## Given
        var responseEntity = administrativeCategoriesRestApiAdapter.createCategory(categoryCreateDto);

        //## Then
        assertNotNull(responseEntity);
        assertEquals(categoryDto, responseEntity.getBody());
        assertEquals(HttpStatus.CREATED.value(), responseEntity.getStatusCodeValue());
        verify(iCreateCategoryUseCase, times(1)).createItem(categoryCreateDto);
    }

    @Test
    void listAllCategories() {

        //## When
        var categorysDto = List.of(
                new CategoryDto("1", "Suco", ZonedDateTime.now(), ZonedDateTime.now())
        );
        when(iSearchCategoryUseCase.items()).thenReturn(categorysDto);

        //## Given
        var responseEntity = administrativeCategoriesRestApiAdapter.listAllCategories();

        //## Then
        assertNotNull(responseEntity);
        assertEquals(categorysDto.get(0), responseEntity.getBody().get(0));
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    void getCategoryByID() {

        //## When
        var categoryDto = new CategoryDto("1", "Suco", ZonedDateTime.now(), ZonedDateTime.now());
        when(iSearchCategoryUseCase.item(anyString())).thenReturn(categoryDto);

        //## Given
        var responseEntity = administrativeCategoriesRestApiAdapter.getCategoryByID(anyString());

        //## Then
        assertNotNull(responseEntity);
        assertEquals(categoryDto, responseEntity.getBody());
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    void deleteCategoryByID() {

        //## When - Given
        var responseEntity = administrativeCategoriesRestApiAdapter.deleteCategoryByID(anyString());

        //## Then
        verify(iDeleteCategoryUseCase).removeItem(anyString());
        assertEquals(HttpStatus.NO_CONTENT.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    void updateCategory() {

        //## When
        var categoryCreateDto = new CategoryCreateDto("Suco");
        var categoryDto = new CategoryDto("1", "Suco", ZonedDateTime.now(), ZonedDateTime.now());
        when(iUpdateCategoryUseCase.updateItem(categoryCreateDto, "1")).thenReturn(categoryDto);

        //## Given
        var responseEntity = administrativeCategoriesRestApiAdapter.updateCategory(categoryCreateDto, "1");

        //## Then
        assertNotNull(responseEntity);
        assertEquals(categoryDto, responseEntity.getBody());
        assertEquals(HttpStatus.ACCEPTED.value(), responseEntity.getStatusCodeValue());
    }

}