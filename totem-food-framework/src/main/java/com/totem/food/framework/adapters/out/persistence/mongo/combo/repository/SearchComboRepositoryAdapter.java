package com.totem.food.framework.adapters.out.persistence.mongo.combo.repository;

import com.totem.food.application.ports.in.dtos.combo.ComboFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.domain.combo.ComboDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.combo.entity.ComboEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.combo.mapper.IComboEntityMapper;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Component
public class SearchComboRepositoryAdapter implements ISearchRepositoryPort<ComboFilterDto, List<ComboDomain>> {

    @Repository
    protected interface ComboRepositoryMongoDB extends BaseRepository<ComboEntity, String> {
        @Query("{ '_id': { $in: ?0 } }")
        List<ComboEntity> findAllByIds(List<String> ids);
    }

    private final ComboRepositoryMongoDB repository;
    private final IComboEntityMapper iComboEntityMapper;

    @Override
    public List<ComboDomain> findAll(ComboFilterDto item) {
        if(CollectionUtils.isNotEmpty(item.getIds()))
            return repository.findAllByIds(item.getIds()).stream().map(iComboEntityMapper::toDomain).toList();
        return List.of();
    }

}
