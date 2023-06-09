package com.totem.food.framework.adapters.out.persistence.mongo.category.repository;

import com.totem.food.application.ports.out.persistence.category.ICategoryRepositoryPort;
import com.totem.food.domain.category.CategoryDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.category.entity.CategoryEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.category.mapper.ICategoryEntityMapper;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Component
public class CategoryRepositoryAdapter implements ICategoryRepositoryPort<CategoryDomain> {

	@Repository
	protected interface CategoryRepositoryMongoDB extends BaseRepository<CategoryEntity, String> {
	}

	private final CategoryRepositoryMongoDB repository;
	private final ICategoryEntityMapper iCategoryEntityMapper;

	@Override
	public CategoryDomain saveItem(CategoryDomain item) {
		final var categoryEntity = iCategoryEntityMapper.toEntity(item);
		final var savedCategoryEntity = repository.save(categoryEntity);
		return iCategoryEntityMapper.toDomain(savedCategoryEntity);
	}

	@Override
	public CategoryDomain removeItem(CategoryDomain item) {
		return null;
	}
}
