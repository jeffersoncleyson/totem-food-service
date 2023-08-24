package com.totem.food.application.usecases.product;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.product.ProductCreateDto;
import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.ports.in.mappers.category.ICategoryMapper;
import com.totem.food.application.ports.in.mappers.product.IProductMapper;
import com.totem.food.application.ports.out.persistence.category.CategoryModel;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.ports.out.persistence.product.ProductModel;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
@UseCase
public class UpdateProductUseCase implements IUpdateUseCase<ProductCreateDto, ProductDto> {

    private final IProductMapper iProductMapper;
    private final ICategoryMapper iCategoryMapper;
    private final IUpdateRepositoryPort<ProductModel> iProductRepositoryPort;
    private final ISearchUniqueRepositoryPort<Optional<ProductModel>> iSearchUniqueRepositoryPort;
    private final ISearchUniqueRepositoryPort<Optional<CategoryModel>> iSearchUniqueCategoryRepositoryPort;

    @Override
    public ProductDto updateItem(ProductCreateDto item, String id) {

        final var productModelOpt = iSearchUniqueRepositoryPort.findById(id);
        final var productDomain = productModelOpt.map(iProductMapper::toDomain)
                .orElseThrow(() -> new ElementNotFoundException(String.format("Product [%s] not found", id)));

        final var categoryModel = iSearchUniqueCategoryRepositoryPort.findById(item.getCategory())
                .orElseThrow(() -> new ElementNotFoundException(String.format("Category [%s] not found", item.getCategory())));

        productDomain.setName(item.getName());
        productDomain.setDescription(item.getDescription());
        productDomain.setImage(item.getImage());
        productDomain.setPrice(item.getPrice());

        final var categoryDomain = iCategoryMapper.toDomain(categoryModel);
        productDomain.setCategory(categoryDomain);


        productDomain.updateModifiedAt();
        final var model = iProductMapper.toModel(productDomain);
        final var domainSaved = iProductRepositoryPort.updateItem(model);
        return iProductMapper.toDto(domainSaved);
    }
}