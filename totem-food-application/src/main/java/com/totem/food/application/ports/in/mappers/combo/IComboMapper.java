package com.totem.food.application.ports.in.mappers.combo;

import com.totem.food.application.ports.in.dtos.combo.ComboCreateDto;
import com.totem.food.application.ports.in.dtos.combo.ComboDto;
import com.totem.food.domain.combo.ComboDomain;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IComboMapper {

    ComboDomain toDomain(ComboCreateDto input);

    ComboDto toDto(ComboDomain input);
}
