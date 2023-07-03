package com.totem.food.framework.adapters.out.persistence.mongo.combo.repository;

import com.totem.food.application.ports.in.dtos.combo.ComboFilterDto;
import com.totem.food.framework.adapters.out.persistence.mongo.combo.mapper.IComboEntityMapper;
import lombok.SneakyThrows;
import mocks.domains.ComboDomainMock;
import mocks.entity.ComboEntityMock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Closeable;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchComboRepositoryAdapterTest {

    @Mock
    private SearchComboRepositoryAdapter.ComboRepositoryMongoDB repository;

    @Spy
    private IComboEntityMapper iComboEntityMapper = Mappers.getMapper(IComboEntityMapper.class);

    private SearchComboRepositoryAdapter searchComboRepositoryAdapter;

    @Mock
    private Closeable closeable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        searchComboRepositoryAdapter = new SearchComboRepositoryAdapter(repository, iComboEntityMapper);
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        closeable.close();
    }

    @Test
    void findAll() {

        //## Mock - Objects
        var comboFilterDto = new ComboFilterDto("Combo da Casa", List.of("1"));
        var comboEntity = ComboEntityMock.getMock();
        var comboDomain = ComboDomainMock.getMock();

        //## Given
        when(repository.findAllByIds(anyList())).thenReturn(List.of(comboEntity));

        //## When
        var result = searchComboRepositoryAdapter.findAll(comboFilterDto);

        //## Then
        Assertions.assertThat(result).usingRecursiveComparison()
                .ignoringFieldsOfTypes(ZonedDateTime.class)
                .isEqualTo(List.of(comboDomain));

        verify(iComboEntityMapper, times(1)).toModel(comboEntity);

    }

    @Test
    void findAllWhenParamNull() {

        //## Given - Mock
        var comboFilterDto = new ComboFilterDto("Combo da Casa", List.of());

        //## When
        var result = searchComboRepositoryAdapter.findAll(comboFilterDto);

        //## Then
        assertTrue(result.isEmpty());
        verify(iComboEntityMapper, never()).toModel(any());

    }
}