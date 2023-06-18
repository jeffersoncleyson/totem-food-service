package com.totem.food.framework.adapters.in.rest.product;

import com.totem.food.application.ports.in.dtos.product.ProductCreateDto;
import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
import com.totem.food.application.ports.in.rest.ICreateRestApiPort;
import com.totem.food.application.ports.in.rest.IRemoveRestApiPort;
import com.totem.food.application.ports.in.rest.ISearchRestApiPort;
import com.totem.food.application.ports.in.rest.ISearchUniqueRestApiPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.application.usecases.commons.IDeleteUseCase;
import com.totem.food.application.usecases.commons.ISearchUniqueUseCase;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/administrative/product")
@AllArgsConstructor
public class AdministrativeProductRestApiAdapter implements
        ICreateRestApiPort<ProductCreateDto, ResponseEntity<ProductDto>>,
        ISearchRestApiPort<ProductFilterDto, ResponseEntity<List<ProductDto>>>,
        ISearchUniqueRestApiPort<String, ResponseEntity<ProductDto>>,
        IRemoveRestApiPort<String, Void> {

    private final ICreateUseCase<ProductCreateDto, ProductDto> createProductUseCase;
    private final ISearchUseCase<ProductFilterDto, List<ProductDto>> iSearchProductUseCase;
    private final ISearchUniqueUseCase<String, Optional<ProductDto>> iSearchUniqueUseCase;
    private final IDeleteUseCase<ProductDto> iDeleteUseCase;

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
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }


    @Override
    @GetMapping(path = "/{productId}")
    public ResponseEntity<ProductDto> getById(@PathVariable String productId) {
        return iSearchUniqueUseCase.item(productId)
                .map(productDto -> ResponseEntity.status(HttpStatus.OK).body(productDto))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @DeleteMapping(path = "/{productId}")
    public ResponseEntity<Void> deleteById(@PathVariable String productId) {
        iDeleteUseCase.removeItem(productId);
        return ResponseEntity.noContent().build();
    }
}
