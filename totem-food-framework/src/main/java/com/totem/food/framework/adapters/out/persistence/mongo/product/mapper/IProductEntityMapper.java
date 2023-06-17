package com.totem.food.framework.adapters.out.persistence.mongo.product.mapper;

import com.totem.food.domain.product.ProductDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.product.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IProductEntityMapper {

	ProductEntity toEntity(ProductDomain input);

	ProductDomain toDomain(ProductEntity input);
}
