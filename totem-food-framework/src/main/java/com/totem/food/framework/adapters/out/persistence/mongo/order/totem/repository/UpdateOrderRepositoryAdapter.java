package com.totem.food.framework.adapters.out.persistence.mongo.order.totem.repository;

import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.ports.out.persistence.order.totem.OrderModel;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.entity.OrderEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.mapper.IOrderEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Component
public class UpdateOrderRepositoryAdapter implements IUpdateRepositoryPort<OrderModel> {

	@Repository
	protected interface ProductRepositoryMongoDB extends BaseRepository<OrderEntity, String> {
	}

	private final ProductRepositoryMongoDB repository;
	private final IOrderEntityMapper iOrderEntityMapper;

	@Override
	public OrderModel updateItem(OrderModel item) {
		final var entity = iOrderEntityMapper.toEntity(item);
		final var savedEntity = repository.save(entity);
		return iOrderEntityMapper.toModel(savedEntity);
	}

}
