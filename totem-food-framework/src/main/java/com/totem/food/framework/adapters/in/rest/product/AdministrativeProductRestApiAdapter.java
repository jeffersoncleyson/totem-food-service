package com.totem.food.framework.adapters.in.rest.product;

import com.totem.food.application.ports.in.dtos.product.ProductCreateDto;
import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
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
import org.springframework.http.MediaType;
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
import java.util.Optional;

import static com.totem.food.framework.adapters.in.rest.constants.Routes.ADM_PRODUCT;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.API_VERSION_1;
import static com.totem.food.framework.adapters.in.rest.constants.Routes.PRODUCT_ID;

@RestController
@RequestMapping(value = API_VERSION_1 + ADM_PRODUCT)
@AllArgsConstructor
public class AdministrativeProductRestApiAdapter implements
        ICreateRestApiPort<ProductCreateDto, ResponseEntity<ProductDto>>,
        ISearchRestApiPort<ProductFilterDto, ResponseEntity<List<ProductDto>>>,
        ISearchUniqueRestApiPort<String, ResponseEntity<ProductDto>>,
        IRemoveRestApiPort<String, ResponseEntity<Void>>,
        IUpdateRestApiPort<ProductCreateDto, ResponseEntity<ProductDto>> {

    private final ICreateUseCase<ProductCreateDto, ProductDto> createProductUseCase;
    private final ISearchUseCase<ProductFilterDto, List<ProductDto>> iSearchProductUseCase;
    private final ISearchUniqueUseCase<String, ProductDto> iSearchUniqueUseCase;
    private final IDeleteUseCase<String, ProductDto> iDeleteUseCase;
    private final IUpdateUseCase<ProductCreateDto, ProductDto> iUpdateUseCase;

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
    @GetMapping(PRODUCT_ID)
    public ResponseEntity<ProductDto> getById(@PathVariable String productId) {
        return Optional.ofNullable(iSearchUniqueUseCase.item(productId))
                .map(productDto -> ResponseEntity.status(HttpStatus.OK).body(productDto))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @DeleteMapping(PRODUCT_ID)
    public ResponseEntity<Void> deleteById(@PathVariable String productId) {
        iDeleteUseCase.removeItem(productId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping(value = PRODUCT_ID, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDto> update(@RequestBody ProductCreateDto item, @PathVariable String productId) {
        return Optional.ofNullable(iUpdateUseCase.updateItem(item, productId))
                .map( productDto -> ResponseEntity.status(HttpStatus.ACCEPTED).body(productDto))
                .orElse(ResponseEntity.notFound().build());
    }
}
