package com.totem.food.application.ports.in.mappers.category;

import com.totem.food.application.ports.in.dtos.category.CategoryCreateDto;
import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.out.category.CategoryModel;
import com.totem.food.domain.category.CategoryDomain;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICategoryMapper {

    CategoryDomain toDomain(CategoryCreateDto input);

    CategoryDto toDto(CategoryModel input);

    CategoryModel toModel(CategoryDomain input);

    CategoryDomain toDomain(CategoryModel input);
}
