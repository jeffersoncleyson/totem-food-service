package com.totem.food.framework.adapters.out.persistence.mongo.product.repository;

import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.ports.out.persistence.product.ProductModel;
import com.totem.food.domain.product.ProductDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.product.entity.ProductEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.product.mapper.IProductEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Component
public class UpdateProductRepositoryAdapter implements IUpdateRepositoryPort<ProductModel> {

	@Repository
	protected interface ProductRepositoryMongoDB extends BaseRepository<ProductEntity, String> {
	}

	private final ProductRepositoryMongoDB repository;
	private final IProductEntityMapper iProductEntityMapper;

	@Override
	public ProductModel updateItem(ProductModel item) {
		final var entity = iProductEntityMapper.toEntity(item);
		final var savedEntity = repository.save(entity);
		return iProductEntityMapper.toModel(savedEntity);
	}

}
