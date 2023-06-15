package com.totem.food.application.usecases.category;

import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.mappers.ICategoryMapper;
import com.totem.food.application.ports.out.persistence.category.ICategoryRepositoryPort;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.domain.category.CategoryDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchCategoryUseCaseTest {

    @Spy
    private ICategoryMapper iCategoryMapper = Mappers.getMapper(ICategoryMapper.class);

    @Mock
    private ICategoryRepositoryPort<CategoryDomain> iCategoryRepositoryPort;

    private ISearchUseCase<String, CategoryDto> iSearchUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.iSearchUseCase = new SearchCategoryUseCase(iCategoryMapper, iCategoryRepositoryPort);
    }

    @Test
    void items() {

        //## Given
        final var categoryDomain = new CategoryDomain("123", "Name", ZonedDateTime.now(ZoneOffset.UTC), ZonedDateTime.now(ZoneOffset.UTC));
        final var domains = List.of(categoryDomain, categoryDomain);
        when(iCategoryRepositoryPort.findAll()).thenReturn(domains);

        //## When
        final var categorysDto = iSearchUseCase.items();

        //## Then
        assertThat(domains).usingRecursiveComparison().isEqualTo(categorysDto);

    }

    @Test
    void item() {

        //## Given
        final var categoryDomain = new CategoryDomain("123", "Name", ZonedDateTime.now(ZoneOffset.UTC), ZonedDateTime.now(ZoneOffset.UTC));
        when(iCategoryRepositoryPort.findById(anyString())).thenReturn(Optional.of(categoryDomain));

        //## When
        final var categoryDto = iSearchUseCase.item(anyString());

        //## Then
        assertThat(categoryDomain).usingRecursiveComparison().isEqualTo(categoryDto);

    }

    @Test
    void itemWithResourceNotFound() {

        //## Given
        when(iCategoryRepositoryPort.findById(anyString())).thenReturn(Optional.empty());

        //## When and Then
        assertThrows(RuntimeException.class, () -> iSearchUseCase.item(anyString()));

        //## Then
        Mockito.verify(iCategoryMapper, never()).toDto(any());

    }
}