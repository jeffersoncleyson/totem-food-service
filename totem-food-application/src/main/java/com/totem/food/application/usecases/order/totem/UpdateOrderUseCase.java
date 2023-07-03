package com.totem.food.application.usecases.order.totem;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.combo.ComboFilterDto;
import com.totem.food.application.ports.in.dtos.order.totem.ItemQuantityDto;
import com.totem.food.application.ports.in.dtos.order.totem.OrderDto;
import com.totem.food.application.ports.in.dtos.order.totem.OrderUpdateDto;
import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
import com.totem.food.application.ports.in.mappers.combo.IComboMapper;
import com.totem.food.application.ports.in.mappers.order.totem.IOrderMapper;
import com.totem.food.application.ports.in.mappers.product.IProductMapper;
import com.totem.food.application.ports.out.persistence.combo.ComboModel;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IUpdateRepositoryPort;
import com.totem.food.application.ports.out.persistence.order.totem.OrderModel;
import com.totem.food.application.ports.out.persistence.product.ProductModel;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.IUpdateUseCase;
import com.totem.food.domain.combo.ComboDomain;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.domain.product.ProductDomain;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@UseCase
public class UpdateOrderUseCase implements IUpdateUseCase<OrderUpdateDto, OrderDto> {

    private final IOrderMapper iOrderMapper;
    private final IProductMapper iProductMapper;
    private final IComboMapper iComboMapper;
    private final ISearchUniqueRepositoryPort<Optional<OrderModel>> iSearchUniqueRepositoryPort;
    private final ISearchRepositoryPort<ComboFilterDto, List<ComboModel>> iSearchComboDomainRepositoryPort;
    private final ISearchRepositoryPort<ProductFilterDto, List<ProductModel>> iSearchProductRepositoryPort;
    private final IUpdateRepositoryPort<OrderModel> iProductRepositoryPort;

    @Override
    public OrderDto updateItem(OrderUpdateDto item, String id) {

        final var orderModelOptional = iSearchUniqueRepositoryPort.findById(id);

        final var orderModel = orderModelOptional
                .orElseThrow(() -> new ElementNotFoundException(String.format("Order [%s] not found", id)));

        setProductsToDomain(item, orderModel);
        setCombosToDomain(item, orderModel);

        final var domain = iOrderMapper.toDomain(orderModel);

        domain.calculatePrice();
        domain.updateModifiedAt();

        final var model = iOrderMapper.toModel(domain);

        final var domainSaved = iProductRepositoryPort.updateItem(model);

        return iOrderMapper.toDto(domainSaved);
    }

    private void setCombosToDomain(OrderUpdateDto item, OrderModel model) {
        if (CollectionUtils.isNotEmpty(item.getCombos())) {

            final var ids = item.getCombos().stream().map(ItemQuantityDto::getId).toList();
            final var comoboFilterDto = ComboFilterDto.builder().ids(ids).build();
            final var combos = iSearchComboDomainRepositoryPort.findAll(comoboFilterDto);

            if (CollectionUtils.size(item.getCombos()) != CollectionUtils.size(combos)) {
                throw new ElementNotFoundException(String.format("Combos [%s] some combos are invalid", ids));
            }

            final var comboDomainToAdd = getComboDomains(item, combos);

            model.setCombos(comboDomainToAdd);
        } else {
            model.setCombos(null);
        }
    }

    // TODO - Refatorar este método
    private List<ComboDomain> getComboDomains(OrderUpdateDto item, List<ComboModel> combos) {
        final var comboDomainMap = combos.stream()
                .collect(Collectors.toMap(ComboModel::getId, iComboMapper::toDomain));
        final var comboDomainToAdd = new ArrayList<ComboDomain>();

        for (ItemQuantityDto itemX : item.getCombos()) {
            for (int i = 0; i < itemX.getQtd(); i++) {
                comboDomainToAdd.add(comboDomainMap.get(itemX.getId()));
            }
        }
        return comboDomainToAdd;
    }

    private void setProductsToDomain(OrderUpdateDto item, OrderModel model) {
        if (CollectionUtils.isNotEmpty(item.getProducts())) {

            final var ids = item.getProducts().stream().map(ItemQuantityDto::getId).toList();
            final var productFilterDto = ProductFilterDto.builder().ids(ids).build();
            final var products = iSearchProductRepositoryPort.findAll(productFilterDto);

            if (CollectionUtils.size(item.getProducts()) != CollectionUtils.size(products)) {
                throw new ElementNotFoundException(String.format("Products [%s] some products are invalid", ids));
            }

            final var productsDomainToAdd = getProductDomains(item, products);
            model.setProducts(productsDomainToAdd);
        } else {
            model.setProducts(null);
        }
    }

    // TODO - Refatorar este método
    private List<ProductDomain> getProductDomains(OrderUpdateDto item, List<ProductModel> products) {
        final var productDomainMap = products.stream()
                .collect(Collectors.toMap(ProductModel::getId, iProductMapper::toDomain));
        final var productsDomainToAdd = new ArrayList<ProductDomain>();

        for (ItemQuantityDto itemX : item.getProducts()) {
            for (int i = 0; i < itemX.getQtd(); i++) {
                productsDomainToAdd.add(productDomainMap.get(itemX.getId()));
            }
        }
        return productsDomainToAdd;
    }
}