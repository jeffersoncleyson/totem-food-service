package com.totem.food.application.usecases.order.totem;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.combo.ComboFilterDto;
import com.totem.food.application.ports.in.dtos.order.totem.OrderDto;
import com.totem.food.application.ports.in.dtos.order.totem.OrderUpdateDto;
import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
import com.totem.food.application.ports.in.mappers.order.totem.IOrderMapper;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import com.totem.food.domain.combo.ComboDomain;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.domain.product.ProductDomain;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@UseCase
public class UpdateOrderUseCase implements IUpdateUseCase<OrderUpdateDto, OrderDto> {

    private final IOrderMapper iOrderMapper;
    private final ISearchUniqueRepositoryPort<Optional<OrderDomain>> iSearchUniqueRepositoryPort;
    private final ISearchRepositoryPort<ComboFilterDto, List<ComboDomain>> iSearchComboDomainRepositoryPort;
    private final ISearchRepositoryPort<ProductFilterDto, List<ProductDomain>> iSearchProductRepositoryPort;
    private final IUpdateRepositoryPort<OrderDomain> iProductRepositoryPort;

    @Override
    public OrderDto updateItem(OrderUpdateDto item, String id) {

        final var orderDomainOpt = iSearchUniqueRepositoryPort.findById(id);

        final var domain = orderDomainOpt
                .orElseThrow(() -> new ElementNotFoundException(String.format("Order [%s] not found", id)));

        setProductsToDomain(item, domain);
        setCombosToDomain(item, domain);

        domain.calculatePrice();
        domain.updateModifiedAt();

        final var domainSaved = iProductRepositoryPort.updateItem(domain);

        return iOrderMapper.toDto(domainSaved);
    }

    private void setCombosToDomain(OrderUpdateDto item, OrderDomain domain) {
        if(CollectionUtils.isNotEmpty(item.getCombos())){

            final var comboFilterDto = ComboFilterDto.builder().ids(item.getCombos()).build();
            final var combos = iSearchComboDomainRepositoryPort.findAll(comboFilterDto);

            if(CollectionUtils.size(item.getCombos()) != CollectionUtils.size(combos)){
                throw new ElementNotFoundException(String.format("Combos [%s] some combos are invalid", item.getProducts()));
            }

            domain.setCombos(combos);
        } else {
            domain.clearCombos();
        }
    }

    private void setProductsToDomain(OrderUpdateDto item, OrderDomain domain) {
        if(CollectionUtils.isNotEmpty(item.getProducts())){

            final var productFilterDto = ProductFilterDto.builder().ids(item.getProducts()).build();
            final var products = iSearchProductRepositoryPort.findAll(productFilterDto);

            if(CollectionUtils.size(item.getProducts()) != CollectionUtils.size(products)){
                throw new ElementNotFoundException(String.format("Products [%s] some products are invalid", item.getProducts()));
            }

            domain.setProducts(products);
        } else {
            domain.clearProducts();
        }
    }
}