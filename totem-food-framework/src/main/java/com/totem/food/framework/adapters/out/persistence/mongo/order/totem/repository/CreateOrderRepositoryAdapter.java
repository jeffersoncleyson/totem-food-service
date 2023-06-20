package com.totem.food.framework.adapters.out.persistence.mongo.order.totem.repository;

import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.order.admin.mapper.IOrderAdminEntityMapper;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.entity.OrderEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.order.totem.mapper.IOrderEntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Component
public class CreateOrderRepositoryAdapter implements ICreateRepositoryPort<OrderDomain> {

	@Repository
	protected interface OrderRepositoryMongoDB extends BaseRepository<OrderEntity, String> {

	}

	private final OrderRepositoryMongoDB repository;
	private final IOrderEntityMapper iOrderEntityMapper;

	@Override
	public OrderDomain saveItem(OrderDomain item) {
		final var entity = iOrderEntityMapper.toEntity(item);
		final var savedEntity = repository.save(entity);
		return iOrderEntityMapper.toDomain(savedEntity);
	}

}
