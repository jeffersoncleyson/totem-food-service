package com.totem.food.framework.adapters.out.persistence.mongo.combo.repository;

import com.totem.food.application.ports.out.persistence.commons.ICreateRepositoryPort;
import com.totem.food.domain.combo.ComboDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.combo.entity.ComboEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.combo.mapper.IComboEntityMapper;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Component
public class CreateComboRepositoryAdapter implements ICreateRepositoryPort<ComboDomain> {

    @Repository
    protected interface ComboRepositoryMongoDB extends BaseRepository<ComboEntity, String> {

    }

    private final ComboRepositoryMongoDB repository;
    private final IComboEntityMapper iCategoryEntityMapper;

    @Override
    public ComboDomain saveItem(ComboDomain item) {
        final var comboEntity = iCategoryEntityMapper.toEntity(item);
        final var savedComboEntity = repository.save(comboEntity);
        return iCategoryEntityMapper.toDomain(savedComboEntity);
    }

}
