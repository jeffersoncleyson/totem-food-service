package com.totem.food.framework.adapters.in.rest.product;

import com.totem.food.application.ports.in.dtos.product.ProductCreateDto;
import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.ports.in.rest.ICreateRestApiPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/administrative/product")
@AllArgsConstructor
public class AdministrativeProductRestApiAdapter implements
        ICreateRestApiPort<ProductCreateDto, ResponseEntity<ProductDto>> {

    private final ICreateUseCase<ProductCreateDto, ProductDto> createProductUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<ProductDto> createItem(@RequestBody ProductCreateDto item) {
        final var createdItem = createProductUseCase.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

}
