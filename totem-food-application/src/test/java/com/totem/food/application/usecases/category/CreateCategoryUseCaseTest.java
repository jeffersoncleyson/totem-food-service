package com.totem.food.application.usecases.category;

import com.totem.food.application.ports.in.dtos.category.CategoryCreateDto;
import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.dtos.category.CategoryFilterDto;
import com.totem.food.application.ports.in.mappers.category.ICategoryMapper;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IExistsRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseTest {

    @Spy
    private ICategoryMapper iCategoryMapper = Mappers.getMapper(ICategoryMapper.class);
    @Mock
    private ICreateRepositoryPort<CategoryDomain> iCreateRepositoryPort;

    @Mock
    private IExistsRepositoryPort<CategoryDomain, Boolean> iSearchRepositoryPort;

    private ICreateUseCase<CategoryCreateDto, CategoryDto> iCreateUseCase;


    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        this.iCreateUseCase = new CreateCategoryUseCase(iCategoryMapper, iCreateRepositoryPort, iSearchRepositoryPort);
    }

    @Test
    void createItem() {

        //## Given
        final var categoryDomain = new CategoryDomain("123", "Name", ZonedDateTime.now(ZoneOffset.UTC), ZonedDateTime.now(ZoneOffset.UTC));
        when(iCreateRepositoryPort.saveItem(Mockito.any(CategoryDomain.class))).thenReturn(categoryDomain);

        //## When
        final var categoryCreateDto = new CategoryCreateDto("Name");
        final var categoryDto = iCreateUseCase.createItem(categoryCreateDto);

        //## Then
        assertEquals(categoryCreateDto.getName(), categoryDto.getName());
        assertThat(categoryDomain).usingRecursiveComparison().isEqualTo(categoryDto);
    }
}