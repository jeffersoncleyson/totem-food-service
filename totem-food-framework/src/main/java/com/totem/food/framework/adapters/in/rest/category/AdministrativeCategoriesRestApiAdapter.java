package com.totem.food.framework.adapters.in.rest.category;

import com.totem.food.application.ports.in.dtos.category.CategoryCreateDto;
import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.rest.ICreateRestApiPort;
import com.totem.food.application.ports.in.rest.IRemoveRestApiPort;
import com.totem.food.application.ports.in.rest.ISearchRestApiPort;
import com.totem.food.application.ports.in.rest.IUpdateRestApiPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.application.usecases.commons.IDeleteUseCase;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        IUpdateRestApiPort<CategoryCreateDto, CategoryDto>,
        ISearchRestApiPort<String, CategoryDto> {

    private final ICreateUseCase<CategoryCreateDto, CategoryDto> iCreateCategoryUseCase;
    private final ISearchUseCase<String, CategoryDto> iSearchCategoryUseCase;
    private final IDeleteUseCase iDeleteCategoryUseCase;
    private final IUpdateUseCase<CategoryCreateDto, CategoryDto> iUpdateCategoryUseCase;

    @PostMapping
    @Override
    public CategoryDto createCategory(@RequestBody CategoryCreateDto item) {
        final var createdItem = iCreateCategoryUseCase.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdItem)
                .getBody();
    }

    @GetMapping
    @Override
    public List<CategoryDto> listAllCategories() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(iSearchCategoryUseCase.items())
                .getBody();
    }

    @GetMapping("/{categoryId}")
    @Override
    public CategoryDto getCategoryByID(@PathVariable String categoryId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(iSearchCategoryUseCase.item(categoryId))
                .getBody();
    }

    @DeleteMapping("/{categoryId}")
    @Override
    public ResponseEntity<Void> deleteCategoryByID(@PathVariable String categoryId) {
        iDeleteCategoryUseCase.removeItem(categoryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/{categoryId}")
    @Override
    public CategoryDto updateCategory(@RequestBody CategoryCreateDto item, @PathVariable String categoryId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(iUpdateCategoryUseCase.updateItem(item, categoryId))
                .getBody();
    }

}