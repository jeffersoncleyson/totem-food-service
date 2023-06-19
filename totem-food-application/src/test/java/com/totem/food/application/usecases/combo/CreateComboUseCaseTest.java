package com.totem.food.application.usecases.combo;

import com.totem.food.application.ports.in.dtos.combo.ComboCreateDto;
import com.totem.food.application.ports.in.dtos.combo.ComboDto;
import com.totem.food.application.ports.in.mappers.combo.IComboMapper;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IExistsRepositoryPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.domain.combo.ComboDomain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateComboUseCaseTest {

    @Spy
    private IComboMapper iCategoryMapper = Mappers.getMapper(IComboMapper.class);
    @Mock
    private ICreateRepositoryPort<ComboDomain> iCreateRepositoryPort;

    @Mock
    private IExistsRepositoryPort<ComboDomain, Boolean> iSearchRepositoryPort;

    private ICreateUseCase<ComboCreateDto, ComboDto> iCreateUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.iCreateUseCase = new CreateComboUseCase(iCategoryMapper, iCreateRepositoryPort, iSearchRepositoryPort);
    }

    @Test
    void createItem() {

        //## Given
        final var comboDomain = new ComboDomain("1", "Combo da casa", BigDecimal.TEN, List.of("1", "2"), ZonedDateTime.now(ZoneOffset.UTC), ZonedDateTime.now(ZoneOffset.UTC));
        when(iCreateRepositoryPort.saveItem(Mockito.any(ComboDomain.class))).thenReturn(comboDomain);

        //## When
        final var comboCreateDto = new ComboCreateDto("Combo da casa", BigDecimal.TEN, List.of("1", "2"));
        final var comboDto = iCreateUseCase.createItem(comboCreateDto);

        //## Then
        assertNotNull(comboDto);
        assertEquals(comboCreateDto.getName(), comboDto.getName());
    }
}