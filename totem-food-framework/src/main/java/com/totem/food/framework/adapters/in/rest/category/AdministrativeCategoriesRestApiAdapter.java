package com.totem.food.framework.adapters.in.rest.category;

import com.totem.food.application.ports.in.dtos.category.CategoryCreateDto;
import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.rest.ICreateRestApiPort;
import com.totem.food.application.ports.in.rest.IRemoveRestApiPort;
import com.totem.food.application.ports.in.rest.ISearchRestApiPort;
import com.totem.food.application.ports.in.rest.IUpdateRestApiPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/administrative/category")
@AllArgsConstructor
public class AdministrativeCategoriesRestApiAdapter implements
        ICreateRestApiPort<CategoryCreateDto, CategoryDto>,
        IRemoveRestApiPort<String, ResponseEntity<Void>>,
        IUpdateRestApiPort<CategoryDto, CategoryDto>,
        ISearchRestApiPort<String, CategoryDto> {

    private final ICreateUseCase<CategoryCreateDto, CategoryDto> createCategoryUseCase;
    private final ISearchUseCase<String, CategoryDto> searchUseCase;

    @PostMapping
    @Override
    public CategoryDto createItem(@RequestBody CategoryCreateDto item) {
        final var createdItem = createCategoryUseCase.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem).getBody();
    }

    @GetMapping
    @Override
    public List<CategoryDto> getItems() {
        return ResponseEntity.ok(searchUseCase.items()).getBody();
    }

    @GetMapping("/{categoryId}")
    @Override
    public CategoryDto getItem(@PathVariable String categoryId) {
        return ResponseEntity.ok(searchUseCase.item(categoryId)).getBody();
    }

    @DeleteMapping("/{categoryId}")
    @Override
    public ResponseEntity<Void> removeItem(@PathVariable String categoryId) {
        searchUseCase.removeItem(categoryId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public CategoryDto updateItem(CategoryDto item) {
        return null;
    }


}
