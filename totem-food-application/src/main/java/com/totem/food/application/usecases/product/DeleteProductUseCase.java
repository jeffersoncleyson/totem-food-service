package com.totem.food.application.usecases.product;

import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.ports.out.persistence.commons.IRemoveRepositoryPort;
import com.totem.food.application.ports.out.persistence.product.ProductModel;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.IDeleteUseCase;
import com.totem.food.domain.product.ProductDomain;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@UseCase
public class DeleteProductUseCase implements IDeleteUseCase<String, ProductDto> {

    private final IRemoveRepositoryPort<ProductModel> iSearchUniqueRepositoryPort;

    @Override
    public ProductDto removeItem(String id) {
        iSearchUniqueRepositoryPort.removeItem(id);
        return null;
    }
}
