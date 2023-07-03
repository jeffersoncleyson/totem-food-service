package com.totem.food.application.ports.in.mappers.combo;

import com.totem.food.application.ports.in.dtos.combo.ComboCreateDto;
import com.totem.food.application.ports.in.dtos.combo.ComboDto;
import com.totem.food.application.ports.out.persistence.combo.ComboModel;
import com.totem.food.domain.combo.ComboDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IComboMapper {

    @Mapping(target = "products", ignore = true)
    ComboDomain toDomain(ComboCreateDto input);

    ComboDomain toDomain(ComboModel input);

    ComboModel toModel(ComboDomain input);

    ComboDto toDto(ComboModel input);
}
