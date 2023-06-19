package com.totem.food.application.usecases.combo;

import com.totem.food.application.exceptions.ElementExistsException;
import com.totem.food.application.exceptions.ElementNotFoundException;
import com.totem.food.application.ports.in.dtos.combo.ComboCreateDto;
import com.totem.food.application.ports.in.dtos.combo.ComboDto;
import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
import com.totem.food.application.ports.in.mappers.combo.IComboMapper;
import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.IExistsRepositoryPort;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.usecases.commons.ICreateUseCase;
import com.totem.food.domain.combo.ComboDomain;
import com.totem.food.domain.product.ProductDomain;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CreateComboUseCase implements ICreateUseCase<ComboCreateDto, ComboDto> {

    private final IComboMapper iComboMapper;
    private final ICreateRepositoryPort<ComboDomain> iCreateRepositoryPort;
    private final IExistsRepositoryPort<ComboDomain, Boolean> iSearchRepositoryPort;
    private final ISearchRepositoryPort<ProductFilterDto, List<ProductDomain>> iSearchProductRepositoryPort;

    @Override
    public ComboDto createItem(ComboCreateDto item) {
        final var comboDomain = iComboMapper.toDomain(item);
        comboDomain.validateCategory();
        comboDomain.fillDates();

        if (Boolean.TRUE.equals(iSearchRepositoryPort.exists(comboDomain))) {
            throw new ElementExistsException(String.format("Combo [%s] already registered", item.getName()));
        }

        final var productsDomainList = iSearchProductRepositoryPort.findAll(ProductFilterDto.builder().ids(item.getProducts()).build());

        if(CollectionUtils.size(productsDomainList) != CollectionUtils.size(item.getProducts())){
            throw new ElementNotFoundException(String.format("Products [%s] some products are invalid", item.getProducts()));
        }

        comboDomain.setProducts(productsDomainList);

        final var comboDomainSaved = iCreateRepositoryPort.saveItem(comboDomain);
        return iComboMapper.toDto(comboDomainSaved);
    }
}
