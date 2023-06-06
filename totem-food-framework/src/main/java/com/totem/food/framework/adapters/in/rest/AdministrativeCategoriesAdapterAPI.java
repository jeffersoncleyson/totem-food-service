package com.totem.food.framework.adapters.in.rest;

import com.totem.food.application.ports.in.dtos.CategoryDto;
import com.totem.food.application.ports.in.rest.IAdministrativeCategoriesPort;
import com.totem.food.application.ports.in.rest.common.IListPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/administrative/category")
@AllArgsConstructor
public class AdministrativeCategoriesAdapterAPI implements
	IAdministrativeCategoriesPort<CategoryDto, ResponseEntity<CategoryDto>>,
	IListPort<CategoryDto, ResponseEntity<List<CategoryDto>>> {

	private final ICreateUseCase<CategoryDto> createCategoyUseCase;

	@Override
	public ResponseEntity<CategoryDto> createItem(CategoryDto item) {
		final var createdItem = createCategoyUseCase.createItem(item);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
	}

	@Override
	public ResponseEntity<CategoryDto> removeItem(CategoryDto item) {
		return null;
	}

	@Override
	public ResponseEntity<CategoryDto> updateItem(CategoryDto item) {
		return null;
	}

	@Override
	public ResponseEntity<List<CategoryDto>> listItem(CategoryDto itemFilter) {
		return null;
	}
}
