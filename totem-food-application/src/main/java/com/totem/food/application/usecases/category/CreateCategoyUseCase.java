package com.totem.food.application.usecases.category;

import com.totem.food.application.ports.in.dtos.CategoryDto;
import com.totem.food.application.ports.in.mappers.ICategoryMapper;
import com.totem.food.application.ports.out.persistence.category.ICategoryRepositoryPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.domain.category.CategoryDomain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CreateCategoyUseCase implements ICreateUseCase<CategoryDto> {

	private final ICategoryMapper iCategoryMapper;
	private final ICategoryRepositoryPort<CategoryDomain> iCategoryRepositoryPort;

	public CategoryDto createItem(CategoryDto item){

		final var categoryDomain = iCategoryMapper.toDomain(item);
		categoryDomain.validateCategory();
		iCategoryRepositoryPort.saveItem(categoryDomain);

		return null;
	}

}
