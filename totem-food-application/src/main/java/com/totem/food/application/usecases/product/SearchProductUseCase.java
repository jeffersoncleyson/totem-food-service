package com.totem.food.application.usecases.product;

import com.totem.food.application.ports.in.dtos.product.ProductDto;
import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
import com.totem.food.application.ports.in.mappers.product.IProductMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.usecases.commons.ISearchUseCase;
import com.totem.food.domain.product.ProductDomain;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SearchProductUseCase implements ISearchUseCase<ProductFilterDto, List<ProductDto>> {

    private final IProductMapper iProductMapper;
    private final ISearchRepositoryPort<ProductFilterDto, List<ProductDomain>> iSearchProductRepositoryPort;

    @Override
    public List<ProductDto> items(ProductFilterDto filter) {


        return Optional.ofNullable(iSearchProductRepositoryPort.findAll(filter))
                .map(productsDomain ->
                        productsDomain.stream().map(iProductMapper::toDto).collect(Collectors.toList()))
                .orElse(List.of());

//        var listDomain = new ArrayList<ProductDto>();
//        for (List<ProductDomain> typeProducts : iSearchProductRepositoryPort.findAll(filter)) {
//            for (ProductDomain productDomain : typeProducts) {
//                listDomain.add(iProductMapper.toDto(productDomain));
//            }
//        }
//
//        return listDomain;
    }
}
