package com.totem.food.framework.adapters.in.rest;

import com.totem.food.application.ports.in.dtos.category.CategoryCreateDto;
import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.dtos.category.FilterCategoryDto;
import com.totem.food.application.ports.in.rest.ICreateRestApiPort;
import com.totem.food.application.ports.in.rest.IRemoveRestApiPort;
import com.totem.food.application.ports.in.rest.ISearchRestApiPort;
import com.totem.food.application.ports.in.rest.IUpdateRestApiPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/administrative/category")
@AllArgsConstructor
public class AdministrativeCategoriesRestApiAdapter implements
        ICreateRestApiPort<CategoryCreateDto, ResponseEntity<CategoryDto>>,
        IRemoveRestApiPort<String, ResponseEntity<Void>>,
        IUpdateRestApiPort<CategoryDto, ResponseEntity<CategoryDto>>,
        ISearchRestApiPort<FilterCategoryDto, ResponseEntity<List<CategoryDto>>> {

    private final ICreateUseCase<CategoryCreateDto, CategoryDto> createCategoryUseCase;
    private final ISearchUseCase<CategoryDto> searchUseCase;

    @PostMapping
    @Override
    public ResponseEntity<CategoryDto> createItem(@RequestBody CategoryCreateDto item) {
        final var createdItem = createCategoryUseCase.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @GetMapping
    @Override
    public ResponseEntity<List<CategoryDto>> getItems() {
        return ResponseEntity.ok(searchUseCase.items());
    }

    @Override
    public ResponseEntity<Void> removeItem(String itemId) {
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CategoryDto> updateItem(CategoryDto item) {
        return null;
    }


}
