package com.totem.food.framework.adapters.out.persistence.mongo.category.mapper;

import com.totem.food.domain.category.CategoryDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.category.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICategoryEntityMapper {

	CategoryEntity toEntity(CategoryDomain input);

	CategoryDomain toDomain(CategoryEntity input);
}
