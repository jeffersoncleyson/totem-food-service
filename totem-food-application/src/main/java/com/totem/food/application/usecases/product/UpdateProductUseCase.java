package com.totem.food.application.usecases.product;

import com.totem.food.application.ports.in.dtos.product.ProductCreateDto;
import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.ports.in.mappers.product.IProductMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import com.totem.food.domain.product.ProductDomain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@AllArgsConstructor
@Service
public class UpdateProductUseCase implements IUpdateUseCase<ProductCreateDto, ProductDto> {

    private final IProductMapper iProductMapper;
    private final IUpdateRepositoryPort<ProductDomain> iProductRepositoryPort;
    private final ISearchUniqueRepositoryPort<ProductDomain> iSearchUniqueRepositoryPort;

    @Override
    public ProductDto updateItem(ProductCreateDto item, String id) {

        final var productDomain = iSearchUniqueRepositoryPort.findById(id);

        if(Objects.nonNull(productDomain)){
            productDomain.setName(item.getName());
            productDomain.setDescription(item.getDescription());
            productDomain.setImage(item.getImage());
            productDomain.setPrice(item.getPrice());
            productDomain.setCategory(item.getCategory());
            productDomain.updateModifiedAt();
            final var domainSaved = iProductRepositoryPort.updateItem(productDomain);
            return iProductMapper.toDto(domainSaved);
        }
        return null;
    }
}