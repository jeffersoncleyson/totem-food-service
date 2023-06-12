package com.totem.food.application.ports.in.mappers;

import com.totem.food.application.ports.in.dtos.category.CategoryCreateDto;
import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.domain.category.CategoryDomain;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component
public interface ICategoryMapper {

    CategoryDomain toDomain(CategoryCreateDto input);

    CategoryDto toDto(CategoryDomain input);
}
