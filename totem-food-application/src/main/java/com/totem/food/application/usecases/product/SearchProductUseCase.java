package com.totem.food.application.usecases.product;

import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
import com.totem.food.application.ports.in.mappers.product.IProductMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.product.ProductModel;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.domain.product.ProductDomain;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@UseCase
public class SearchProductUseCase implements ISearchUseCase<ProductFilterDto, List<ProductDto>> {

    private final IProductMapper iProductMapper;
    private final ISearchRepositoryPort<ProductFilterDto, List<ProductModel>> iSearchProductRepositoryPort;

    @Override
    public List<ProductDto> items(ProductFilterDto filter) {
        return Optional.ofNullable(iSearchProductRepositoryPort.findAll(filter))
                .map(productsDomain ->
                        productsDomain.stream().map(iProductMapper::toDto).toList())
                .orElse(List.of());
    }
}
