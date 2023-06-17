package com.totem.food.application.usecases.category;

import com.totem.food.application.ports.in.dtos.category.CategoryCreateDto;
import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.dtos.category.FilterCategoryDto;
import com.totem.food.application.ports.in.mappers.category.ICategoryMapper;
import com.totem.food.application.ports.out.persistence.category.ICategoryRepositoryPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import com.totem.food.domain.category.CategoryDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCategoryUseCaseTest {

    @Spy
    private ICategoryMapper iCategoryMapper = Mappers.getMapper(ICategoryMapper.class);

    @Mock
    private ICategoryRepositoryPort<FilterCategoryDto, CategoryDomain> iCategoryRepositoryPort;


    private IUpdateUseCase<CategoryCreateDto, CategoryDto> iUpdateUseCase;

    @Mock
    private ICreateUseCase<CategoryCreateDto, CategoryDto> iCreateUseCase;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
        this.iUpdateUseCase = new UpdateCategoryUseCase(iCategoryMapper, iCategoryRepositoryPort, iCreateUseCase);
    }

    @Test
    void updateItemWhenCategoryExist() {

        //## Given
        final var categoryDomain = new CategoryDomain("123", "Name", ZonedDateTime.now(ZoneOffset.UTC), ZonedDateTime.now(ZoneOffset.UTC));
        when(iCategoryRepositoryPort.findById(anyString())).thenReturn(Optional.of(categoryDomain));
        categoryDomain.updateModifiedAt();
        when(iCategoryRepositoryPort.updateItem(any(CategoryDomain.class))).thenReturn(categoryDomain);

        //## When
        final var categoryCreateDto = new CategoryCreateDto("name");
        final var categoryDto = iUpdateUseCase.updateItem(categoryCreateDto, anyString());

        //## Then
        assertEquals(categoryDomain.getName(), categoryDto.getName());
        verify(iCategoryMapper).toDto(any());

    }

    @Test
    void createItemWhenCategoryNotFound() {

        //## Given
        final var categoryCreateDto = new CategoryCreateDto("Name");

        //## Then
        assertDoesNotThrow(() -> iUpdateUseCase.updateItem(categoryCreateDto, "123"));

    }
}