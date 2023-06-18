package com.totem.food.application.usecases.category;

import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.dtos.category.CategoryFilterDto;
import com.totem.food.application.ports.in.mappers.category.ICategoryMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.usecases.commons.ISearchUseCase;
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
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchCategoryUseCaseTest {

    @Spy
    private ICategoryMapper iCategoryMapper = Mappers.getMapper(ICategoryMapper.class);

    @Mock
    private ISearchRepositoryPort<CategoryFilterDto, List<CategoryDomain>> iSearchRepositoryPort;

    private ISearchUseCase<CategoryFilterDto, List<CategoryDto>> iSearchUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.iSearchUseCase = new SearchCategoryUseCase(iCategoryMapper, iSearchRepositoryPort);
    }

    @Test
    void items() {

        //## Given
        final var categoryDomain = new CategoryDomain("123", "Name", ZonedDateTime.now(ZoneOffset.UTC), ZonedDateTime.now(ZoneOffset.UTC));
        final var domains = List.of(categoryDomain, categoryDomain);
        final var categoryFilter = new CategoryFilterDto("Name");
        when(iSearchRepositoryPort.findAll(categoryFilter)).thenReturn(domains);

        //## When
        final var categorysDto = iSearchUseCase.items(categoryFilter);

        //## Then
        assertThat(domains).usingRecursiveComparison().isEqualTo(categorysDto);

    }


}