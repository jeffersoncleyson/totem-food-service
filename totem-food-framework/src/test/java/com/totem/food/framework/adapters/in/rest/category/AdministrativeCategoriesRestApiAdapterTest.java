package com.totem.food.framework.adapters.in.rest.category;

import com.totem.food.application.ports.in.dtos.category.CategoryCreateDto;
import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.dtos.category.FilterCategoryDto;
import com.totem.food.application.usecases.category.SearchCategoryUseCase;
import com.totem.food.application.usecases.commons.*;
import com.totem.food.domain.exceptions.ResourceNotFound;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdministrativeCategoriesRestApiAdapterTest {

    @Mock
    private ICreateUseCase<CategoryCreateDto, CategoryDto> iCreateCategoryUseCase;

    @Mock
    private ISearchUseCase<FilterCategoryDto, List<CategoryDto>> iSearchCategoryUseCase;

    @Mock
    private ISearchUniqueUseCase<String, CategoryDto> iSearchUniqueUseCase;

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
        var responseEntity = administrativeCategoriesRestApiAdapter.create(categoryCreateDto);

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
        var categoryFilter = new FilterCategoryDto("Name");
        when(iSearchCategoryUseCase.items(categoryFilter)).thenReturn(categorysDto);

        //## Given
        var responseEntity = administrativeCategoriesRestApiAdapter.listAll(categoryFilter);

        //## Then
        assertNotNull(responseEntity);
        assertEquals(categorysDto.get(0), responseEntity.getBody().get(0));
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    void getCategoryByID() {

        //## When
        var categoryDto = new CategoryDto("1", "Suco", ZonedDateTime.now(), ZonedDateTime.now());
        when(iSearchUniqueUseCase.item(anyString())).thenReturn(categoryDto);

        //## Given
        var responseEntity = administrativeCategoriesRestApiAdapter.getById(anyString());

        //## Then
        assertNotNull(responseEntity);
        assertEquals(categoryDto, responseEntity.getBody());
        assertEquals(HttpStatus.OK.value(), responseEntity.getStatusCodeValue());
    }

    @Test
    void getCategoryByIDWhenNotFound() {

        //## When
        when(iSearchUniqueUseCase.item(anyString())).thenThrow(ResourceNotFound.class);

        //## Given
        assertThrows(ResourceNotFound.class,
                () -> administrativeCategoriesRestApiAdapter.getById(anyString()));

        //## Then
        verify(iSearchUniqueUseCase, times(1)).item(anyString());
    }

    @Test
    void deleteCategoryByID() {

        //## When - Given
        var responseEntity = administrativeCategoriesRestApiAdapter.deleteById(anyString());

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
        var responseEntity = administrativeCategoriesRestApiAdapter.update(categoryCreateDto, "1");

        //## Then
        assertNotNull(responseEntity);
        assertEquals(categoryDto, responseEntity.getBody());
        assertEquals(HttpStatus.ACCEPTED.value(), responseEntity.getStatusCodeValue());
    }

}