package com.totem.food.framework.adapters.out.persistence.mongo.customer.repository;

import com.totem.food.application.ports.out.persistence.commons.IRemoveRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.customer.entity.CustomerEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Component
public class DeleteCustomerRepositoryAdapter implements IRemoveRepositoryPort<CustomerModel> {

	@Repository
	protected interface CustomerRepositoryMongoDB extends BaseRepository<CustomerEntity, String> {
	}

	private final CustomerRepositoryMongoDB repository;

	@Override
	public void removeItem(String id) {
		repository.deleteById(id);
	}
}
