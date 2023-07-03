package com.totem.food.framework.adapters.out.persistence.mongo.combo.mapper;

import com.totem.food.application.ports.out.persistence.combo.ComboModel;
import com.totem.food.domain.combo.ComboDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.combo.entity.ComboEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IComboEntityMapper {

    ComboEntity toEntity(ComboModel input);

    ComboModel toModel(ComboEntity input);
}
