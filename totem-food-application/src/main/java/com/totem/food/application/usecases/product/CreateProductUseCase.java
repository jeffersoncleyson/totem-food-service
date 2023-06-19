package com.totem.food.application.usecases.product;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.product.ProductCreateDto;
import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.ports.in.mappers.product.IProductMapper;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.domain.category.CategoryDomain;
import com.totem.food.domain.product.ProductDomain;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
@UseCase
public class CreateProductUseCase implements ICreateUseCase<ProductCreateDto, ProductDto> {

    private final IProductMapper iProductMapper;
    private final ICreateRepositoryPort<ProductDomain> iProductRepositoryPort;
    private final ISearchUniqueRepositoryPort<Optional<CategoryDomain>> iSearchRepositoryPort;

    public ProductDto createItem(ProductCreateDto item) {
        final var domain = iProductMapper.toDomain(item);
        final var categoryDomain = iSearchRepositoryPort.findById(item.getCategory())
                .orElseThrow(() -> new ElementNotFoundException(String.format("Category [%s] not found", item.getCategory())));
        domain.setCategory(categoryDomain);
        domain.fillDates();
        final var domainSaved = iProductRepositoryPort.saveItem(domain);
        return iProductMapper.toDto(domainSaved);
    }

}