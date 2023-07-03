package com.totem.food.application.usecases.order.totem;

import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.exceptions.InvalidInput;
import com.totem.food.application.ports.in.dtos.combo.ComboFilterDto;
import com.totem.food.application.ports.in.dtos.order.totem.ItemQuantityDto;
import com.totem.food.application.ports.in.dtos.order.totem.OrderCreateDto;
import com.totem.food.application.ports.in.dtos.order.totem.OrderDto;
import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
import com.totem.food.application.ports.in.mappers.combo.IComboMapper;
import com.totem.food.application.ports.in.mappers.customer.ICustomerMapper;
import com.totem.food.application.ports.in.mappers.order.totem.IOrderMapper;
import com.totem.food.application.ports.in.mappers.product.IProductMapper;
import com.totem.food.application.ports.out.persistence.combo.ComboModel;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchUniqueRepositoryPort;
import com.totem.food.application.ports.out.persistence.customer.CustomerModel;
import com.totem.food.application.ports.out.persistence.order.totem.OrderModel;
import com.totem.food.application.ports.out.persistence.product.ProductModel;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@UseCase
public class CreateOrderUseCase implements ICreateUseCase<OrderCreateDto, OrderDto> {

    private final IOrderMapper iOrderMapper;
    private final ICustomerMapper iCustomerMapper;
    private final IProductMapper iProductMapper;
    private final IComboMapper iComboMapper;
    private final ICreateRepositoryPort<OrderModel> iCreateRepositoryPort;
    private final ISearchUniqueRepositoryPort<Optional<CustomerModel>> iSearchUniqueCustomerRepositoryPort;
    private final ISearchRepositoryPort<ProductFilterDto, List<ProductModel>> iSearchProductRepositoryPort;
    private final ISearchRepositoryPort<ComboFilterDto, List<ComboModel>> iSearchDomainRepositoryPort;

    @Override
    public OrderDto createItem(OrderCreateDto item) {

        if(!item.isOrderValid()) {
            throw new InvalidInput("Order is invalid");
        }

        final var domain = new OrderDomain();

        setCustomer(item, domain);
        setProductsToDomain(item, domain);
        setCombosToDomain(item, domain);

        domain.updateOrderStatus(OrderStatusEnumDomain.NEW);
        domain.calculatePrice();
        domain.fillDates();

        final var model = iOrderMapper.toModel(domain);
        final var domainSaved = iCreateRepositoryPort.saveItem(model);
        return iOrderMapper.toDto(domainSaved);
    }

    private void setCustomer(OrderCreateDto item, OrderDomain domain) {

        if(StringUtils.isNotEmpty(item.getCustomerId())) {
            final var customerModel = iSearchUniqueCustomerRepositoryPort.findById(item.getCustomerId())
                    .orElseThrow(() -> new ElementNotFoundException(String.format("Customer [%s] not found", item.getCustomerId())));
            final var customerDomain = iCustomerMapper.toDomain(customerModel);
            domain.setCustomer(customerDomain);
        }
    }

    private void setCombosToDomain(OrderCreateDto item, OrderDomain domain) {
        if(CollectionUtils.isNotEmpty(item.getCombos())){

            final var ids = item.getCombos().stream().map(ItemQuantityDto::getId).toList();
            final var productFilterDto = ComboFilterDto.builder().ids(ids).build();
            final var combos = iSearchDomainRepositoryPort.findAll(productFilterDto);

            if(CollectionUtils.size(item.getCombos()) != CollectionUtils.size(combos)){
                throw new ElementNotFoundException(String.format("Combos [%s] some combos are invalid", ids));
            }

            final var comboDomainToAdd = getComboDomains(item, combos);

            domain.setCombos(comboDomainToAdd);
        }
    }

    // TODO - Refatorar este método
    private List<ComboDomain> getComboDomains(OrderCreateDto item, List<ComboModel> combos) {
        final var comboDomainMap = combos.stream().collect(Collectors.toMap(ComboModel::getId, iComboMapper::toDomain));
        final var comboDomainToAdd = new ArrayList<ComboDomain>();

        for (ItemQuantityDto itemX : item.getCombos()) {
            for (int i = 0; i < itemX.getQtd(); i++) {
                comboDomainToAdd.add(comboDomainMap.get(itemX.getId()));
            }
        }
        return comboDomainToAdd;
    }

    private void setProductsToDomain(OrderCreateDto item, OrderDomain domain) {
        if(CollectionUtils.isNotEmpty(item.getProducts())){

            final var ids = item.getProducts().stream().map(ItemQuantityDto::getId).toList();
            final var productFilterDto = ProductFilterDto.builder().ids(ids).build();
            final var products = iSearchProductRepositoryPort.findAll(productFilterDto);

            if(CollectionUtils.size(item.getProducts()) != CollectionUtils.size(products)){
                throw new ElementNotFoundException(String.format("Products [%s] some products are invalid", ids));
            }

            final var productsDomainToAdd = getProductDomains(item, products);
            domain.setProducts(productsDomainToAdd);
        }
    }

    // TODO - Refatorar este método
    private List<ProductDomain> getProductDomains(OrderCreateDto item, List<ProductModel> products) {
        final var productDomainMap = products.stream().collect(Collectors.toMap(ProductModel::getId, iProductMapper::toDomain));
        final var productsDomainToAdd = new ArrayList<ProductDomain>();

        for (ItemQuantityDto itemX : item.getProducts()) {
            for (int i = 0; i < itemX.getQtd(); i++) {
                productsDomainToAdd.add(productDomainMap.get(itemX.getId()));
            }
        }
        return productsDomainToAdd;
    }
}