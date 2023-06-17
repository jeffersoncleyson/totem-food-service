package com.totem.food.application.ports.in.mappers.product;

import com.totem.food.application.ports.in.dtos.product.ProductCreateDto;
import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.domain.product.ProductDomain;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IProductMapper {

    ProductDomain toDomain(ProductCreateDto input);

    ProductDto toDto(ProductDomain input);
}
