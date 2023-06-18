package com.totem.food.framework.adapters.in.rest.category;

import com.totem.food.application.ports.in.dtos.category.CategoryCreateDto;
import com.totem.food.application.ports.in.dtos.category.CategoryDto;
import com.totem.food.application.ports.in.dtos.category.CategoryFilterDto;
import com.totem.food.application.ports.in.rest.ICreateRestApiPort;
import com.totem.food.application.ports.in.rest.IRemoveRestApiPort;
import com.totem.food.application.ports.in.rest.ISearchRestApiPort;
import com.totem.food.application.ports.in.rest.ISearchUniqueRestApiPort;
import com.totem.food.application.ports.in.rest.IUpdateRestApiPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.application.usecases.commons.IDeleteUseCase;
import com.totem.food.application.usecases.commons.ISearchUniqueUseCase;
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
        ICreateRestApiPort<CategoryCreateDto, ResponseEntity<CategoryDto>>,
        IRemoveRestApiPort<String, ResponseEntity<Void>>,
        IUpdateRestApiPort<CategoryCreateDto, ResponseEntity<CategoryDto>>,
        ISearchRestApiPort<CategoryFilterDto, ResponseEntity<List<CategoryDto>>>,
        ISearchUniqueRestApiPort<String, ResponseEntity<CategoryDto>> {

    private final ICreateUseCase<CategoryCreateDto, CategoryDto> iCreateCategoryUseCase;
    private final ISearchUseCase<CategoryFilterDto, List<CategoryDto>> iSearchCategoryUseCase;
    private final ISearchUniqueUseCase<String, CategoryDto> iSearchUniqueCategoryUseCase;
    private final IDeleteUseCase<String, CategoryDto> iDeleteCategoryUseCase;
    private final IUpdateUseCase<CategoryCreateDto, CategoryDto> iUpdateCategoryUseCase;

    @PostMapping
    @Override
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryCreateDto item) {
        final var createdItem = iCreateCategoryUseCase.createItem(item);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @GetMapping
    @Override
    public ResponseEntity<List<CategoryDto>> listAll(@RequestBody(required = false) CategoryFilterDto filter) {
        return new ResponseEntity<>(iSearchCategoryUseCase.items(filter), HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    @Override
    public ResponseEntity<CategoryDto> getById(@PathVariable String categoryId) {
        return new ResponseEntity<>(iSearchUniqueCategoryUseCase.item(categoryId), HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable String categoryId) {
        iDeleteCategoryUseCase.removeItem(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{categoryId}")
    @Override
    public ResponseEntity<CategoryDto> update(@RequestBody CategoryCreateDto item, @PathVariable String categoryId) {
        return new ResponseEntity<>(iUpdateCategoryUseCase.updateItem(item, categoryId), HttpStatus.ACCEPTED);
    }

}
