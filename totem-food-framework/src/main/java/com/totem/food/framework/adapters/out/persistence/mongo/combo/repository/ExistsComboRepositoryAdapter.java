package com.totem.food.framework.adapters.out.persistence.mongo.combo.repository;

import com.totem.food.application.ports.out.persistence.commons.IExistsRepositoryPort;
import com.totem.food.domain.combo.ComboDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.combo.entity.ComboEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Component
public class ExistsComboRepositoryAdapter implements IExistsRepositoryPort<ComboDomain, Boolean> {


    @Repository
    protected interface ComboRepositoryMongoDB extends BaseRepository<ComboEntity, String> {
        boolean existsByNameIgnoreCase(String name);
    }

    private final ComboRepositoryMongoDB repository;

    @Override
    public Boolean exists(ComboDomain item) {
        return repository.existsByNameIgnoreCase(item.getName());
    }
}
