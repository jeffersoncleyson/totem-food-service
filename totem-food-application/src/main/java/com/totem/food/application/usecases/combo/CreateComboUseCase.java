package com.totem.food.application.usecases.combo;

import com.totem.food.application.ports.in.dtos.combo.ComboCreateDto;
import com.totem.food.application.ports.in.dtos.combo.ComboDto;
import com.totem.food.application.ports.in.mappers.combo.IComboMapper;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IExistsRepositoryPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.domain.combo.ComboDomain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CreateComboUseCase implements ICreateUseCase<ComboCreateDto, ComboDto> {

    private final IComboMapper iComboMapper;
    private final ICreateRepositoryPort<ComboDomain> iCreateRepositoryPort;
    private final IExistsRepositoryPort<ComboDomain, Boolean> iSearchRepositoryPort;


    @Override
    public ComboDto createItem(ComboCreateDto item) {
        final var comboDomain = iComboMapper.toDomain(item);
        comboDomain.validateCategory();
        comboDomain.fillDates();

        if (Boolean.TRUE.equals(iSearchRepositoryPort.exists(comboDomain))) {
            throw new IllegalArgumentException("Combo already registered in base.");
        }

        final var comboDomainSaved = iCreateRepositoryPort.saveItem(comboDomain);
        return iComboMapper.toDto(comboDomainSaved);
    }
}
