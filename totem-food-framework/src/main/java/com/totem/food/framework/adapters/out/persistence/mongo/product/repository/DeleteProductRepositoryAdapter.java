package com.totem.food.framework.adapters.out.persistence.mongo.product.repository;

import com.totem.food.application.ports.out.persistence.commons.IRemoveRepositoryPort;
import com.totem.food.domain.product.ProductDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.product.entity.ProductEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.product.mapper.IProductEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Component
public class DeleteProductRepositoryAdapter implements IRemoveRepositoryPort<ProductDomain> {

	@Repository
	protected interface ProductRepositoryMongoDB extends BaseRepository<ProductEntity, String> {
	}

	private final ProductRepositoryMongoDB repository;

	@Override
	public void removeItem(String id) {
		repository.deleteById(id);
	}
}
