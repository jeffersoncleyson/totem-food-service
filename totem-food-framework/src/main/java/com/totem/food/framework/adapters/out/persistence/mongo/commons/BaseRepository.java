package com.totem.food.framework.adapters.out.persistence.mongo.commons;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

@NoRepositoryBean
public interface BaseRepository<S, K> extends PagingAndSortingRepository<S, K>, QueryByExampleExecutor<S> {
}
