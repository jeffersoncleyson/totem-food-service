package com.totem.food.framework.adapters.out.persistence.mongo.product.repository;

import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
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
public class SearchProductRepositoryAdapter implements ISearchRepositoryPort<ProductFilterDto, List<ProductDomain>> {

	@Repository
	protected interface ProductRepositoryMongoDB extends BaseRepository<ProductEntity, String> {
		List<ProductEntity> findByFilter();
	}

	private final ProductRepositoryMongoDB repository;
	private final IProductEntityMapper iProductEntityMapper;

	@Override
	public List<ProductDomain> findAll(ProductFilterDto filter) {
		return repository.findByFilter().stream().map(iProductEntityMapper::toDomain).toList();
	}
}
