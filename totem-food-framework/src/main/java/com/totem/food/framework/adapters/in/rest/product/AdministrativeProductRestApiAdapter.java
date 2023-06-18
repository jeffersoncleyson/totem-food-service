package com.totem.food.framework.adapters.in.rest.product;

import com.totem.food.application.ports.in.dtos.product.ProductCreateDto;
import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
import com.totem.food.application.ports.in.rest.ICreateRestApiPort;
import com.totem.food.application.ports.in.rest.ISearchRestApiPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/administrative/product")
@AllArgsConstructor
public class AdministrativeProductRestApiAdapter implements
        ICreateRestApiPort<ProductCreateDto, ResponseEntity<ProductDto>>, ISearchRestApiPort<ProductFilterDto, ResponseEntity<List<ProductDto>>> {

    private final ICreateUseCase<ProductCreateDto, ProductDto> createProductUseCase;
    private final ISearchUseCase<ProductFilterDto, List<ProductDto>> iSearchProductUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<ProductDto> create(@RequestBody ProductCreateDto item) {
        final var createdItem = createProductUseCase.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductDto>> listAll(ProductFilterDto filter) {
        final var products = iSearchProductUseCase.items(filter);
        return ResponseEntity.status(HttpStatus.CREATED).body(products);
    }
}
