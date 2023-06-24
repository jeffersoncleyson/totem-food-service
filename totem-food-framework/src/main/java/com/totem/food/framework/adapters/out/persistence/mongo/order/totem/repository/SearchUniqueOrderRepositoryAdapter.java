package com.totem.food.framework.adapters.out.persistence.mongo.order.totem.repository;

import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.entity.OrderEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.mapper.IOrderEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@AllArgsConstructor
@Component
public class SearchUniqueOrderRepositoryAdapter implements ISearchUniqueRepositoryPort<Optional<OrderDomain>> {

	@Repository
	protected interface ProductRepositoryMongoDB extends BaseRepository<OrderEntity, String> {
	}

	private final ProductRepositoryMongoDB repository;
	private final IOrderEntityMapper iOrderEntityMapper;

	@Override
	public Optional<OrderDomain> findById(String id) {
		return repository.findById(id).map(iOrderEntityMapper::toDomain);
	}
}
