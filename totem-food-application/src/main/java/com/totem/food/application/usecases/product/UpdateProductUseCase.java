package com.totem.food.application.usecases.product;

import com.totem.food.application.ports.in.dtos.product.ProductCreateDto;
import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.ports.in.mappers.product.IProductMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import com.totem.food.domain.category.CategoryDomain;
import com.totem.food.domain.product.ProductDomain;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
@UseCase
public class UpdateProductUseCase implements IUpdateUseCase<ProductCreateDto, ProductDto> {

    private final IProductMapper iProductMapper;
    private final IUpdateRepositoryPort<ProductDomain> iProductRepositoryPort;
    private final ISearchUniqueRepositoryPort<Optional<ProductDomain>> iSearchUniqueRepositoryPort;
    private final ISearchUniqueRepositoryPort<Optional<CategoryDomain>> iSearchUniqueCategoryRepositoryPort;

    @Override
    public ProductDto updateItem(ProductCreateDto item, String id) {

        final var productDomainOpt = iSearchUniqueRepositoryPort.findById(id);
        final var productDomain = productDomainOpt.orElseThrow();

        final var categoryDomain = iSearchUniqueCategoryRepositoryPort.findById(item.getCategory()).orElseThrow();

        productDomain.setName(item.getName());
        productDomain.setDescription(item.getDescription());
        productDomain.setImage(item.getImage());
        productDomain.setPrice(item.getPrice());
        productDomain.setCategory(categoryDomain);
        productDomain.updateModifiedAt();

        final var domainSaved = iProductRepositoryPort.updateItem(productDomain);
        return iProductMapper.toDto(domainSaved);
    }
}