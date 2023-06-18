package com.totem.food.application.usecases.product;

import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.ports.out.persistence.commons.IRemoveRepositoryPort;
import com.totem.food.application.usecases.commons.IDeleteUseCase;
import com.totem.food.domain.product.ProductDomain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DeleteProductUseCase implements IDeleteUseCase<String, ProductDto> {

    private final IRemoveRepositoryPort<ProductDomain> iSearchUniqueRepositoryPort;

    @Override
    public ProductDto removeItem(String id) {
        iSearchUniqueRepositoryPort.removeItem(id);
        return null;
    }
}
