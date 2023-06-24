package com.totem.food.application.usecases.order.totem;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.combo.ComboFilterDto;
import com.totem.food.application.ports.in.dtos.order.totem.OrderCreateDto;
import com.totem.food.application.ports.in.dtos.order.totem.OrderDto;
import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
import com.totem.food.application.ports.in.mappers.order.totem.IOrderMapper;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.usecases.annotations.UseCase;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.domain.combo.ComboDomain;
import com.totem.food.domain.customer.CustomerDomain;
import com.totem.food.domain.order.enums.OrderStatusEnumDomain;
import com.totem.food.domain.order.totem.OrderDomain;
import com.totem.food.domain.product.ProductDomain;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@UseCase
public class CreateOrderUseCase implements ICreateUseCase<OrderCreateDto, OrderDto> {

    private final IOrderMapper iOrderMapper;
    private final ICreateRepositoryPort<OrderDomain> iCreateRepositoryPort;
    private final ISearchUniqueRepositoryPort<Optional<CustomerDomain>> iSearchUniqueCustomerRepositoryPort;
    private final ISearchRepositoryPort<ProductFilterDto, List<ProductDomain>> iSearchProductRepositoryPort;
    private final ISearchRepositoryPort<ComboFilterDto, List<ComboDomain>> iSearchDomainRepositoryPort;

    @Override
    public OrderDto createItem(OrderCreateDto item) {

        final var domain = new OrderDomain();

        setCustomer(item, domain);
        setProductsToDomain(item, domain);
        setCombosToDomain(item, domain);

        domain.updateOrderStatus(OrderStatusEnumDomain.NEW);
        domain.calculatePrice();
        domain.fillDates();

        final var domainSaved = iCreateRepositoryPort.saveItem(domain);
        return iOrderMapper.toDto(domainSaved);
    }

    private void setCustomer(OrderCreateDto item, OrderDomain domain) {

        if(StringUtils.isNotEmpty(item.getCustomerId())) {
            final var customerDomain = iSearchUniqueCustomerRepositoryPort.findById(item.getCustomerId())
                    .orElseThrow(() -> new ElementNotFoundException(String.format("Customer [%s] not found", item.getCustomerId())));

            domain.setCustomer(customerDomain);
        }
    }

    private void setCombosToDomain(OrderCreateDto item, OrderDomain domain) {
        if(CollectionUtils.isNotEmpty(item.getCombos())){

            final var productFilterDto = ComboFilterDto.builder().ids(item.getCombos()).build();
            final var combos = iSearchDomainRepositoryPort.findAll(productFilterDto);

            if(CollectionUtils.size(item.getCombos()) != CollectionUtils.size(combos)){
                throw new ElementNotFoundException(String.format("Combos [%s] some combos are invalid", item.getProducts()));
            }

            domain.setCombos(combos);
        }
    }

    private void setProductsToDomain(OrderCreateDto item, OrderDomain domain) {
        if(CollectionUtils.isNotEmpty(item.getProducts())){

            final var productFilterDto = ProductFilterDto.builder().ids(item.getProducts()).build();
            final var products = iSearchProductRepositoryPort.findAll(productFilterDto);

            if(CollectionUtils.size(item.getProducts()) != CollectionUtils.size(products)){
                throw new ElementNotFoundException(String.format("Products [%s] some products are invalid", item.getProducts()));
            }

            domain.setProducts(products);
        }
    }
}