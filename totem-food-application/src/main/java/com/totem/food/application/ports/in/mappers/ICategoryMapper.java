package com.totem.food.application.ports.in.mappers;

import com.totem.food.application.ports.in.dtos.CategoryDto;
import com.totem.food.domain.category.CategoryDomain;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICategoryMapper {

	CategoryDomain toDomain(CategoryDto input);

	CategoryDto toDto(CategoryDomain input);
}
