package com.totem.food.framework.adapters.out.persistence.mongo.product.repository;

import com.mongodb.DBRef;
import com.totem.food.application.ports.in.dtos.product.ProductFilterDto;
import com.totem.food.application.ports.out.persistence.commons.ISearchRepositoryPort;
import com.totem.food.application.ports.out.persistence.product.ProductModel;
import com.totem.food.domain.product.ProductDomain;
import com.totem.food.framework.adapters.out.persistence.mongo.commons.BaseRepository;
import com.totem.food.framework.adapters.out.persistence.mongo.product.entity.ProductEntity;
import com.totem.food.framework.adapters.out.persistence.mongo.product.mapper.IProductEntityMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@AllArgsConstructor
@Component
public class SearchProductRepositoryAdapter implements ISearchRepositoryPort<ProductFilterDto, List<ProductModel>> {

    @Repository
    protected interface ProductRepositoryMongoDB extends BaseRepository<ProductEntity, String> {

        @org.springframework.data.mongodb.repository.Query("{'name': ?0}")
        List<ProductEntity> findByFilter(String name);

        @org.springframework.data.mongodb.repository.Query("{ '_id': { $in: ?0 } }")
        List<ProductEntity> findAllByIds(List<String> ids);

        List<ProductEntity> findAll();
    }

    private final ProductRepositoryMongoDB repository;
    private final MongoTemplate mongoTemplate;
    private final IProductEntityMapper iProductEntityMapper;

    @Override
    public List<ProductModel> findAll(ProductFilterDto filter) {
        var query = new Query();

        if (CollectionUtils.isNotEmpty(filter.getIds()))
            return repository.findAllByIds(filter.getIds()).stream().map(iProductEntityMapper::toModel).toList();

        if (StringUtils.isNotEmpty(filter.getName()))
            return repository.findByFilter(filter.getName()).stream().map(iProductEntityMapper::toModel).toList();

        if (CollectionUtils.isNotEmpty(filter.getCategoryId())) {
            final var objectIds = filter.getCategoryId()
                    .stream()
                    .map(id -> new DBRef("category", new ObjectId(id)))
                    .toList();
            query = Query.query(Criteria.where("category").in(objectIds));
        }

        List<ProductEntity> products = mongoTemplate.find(query, ProductEntity.class);
        return products.stream().map(iProductEntityMapper::toModel).toList();
    }
}
