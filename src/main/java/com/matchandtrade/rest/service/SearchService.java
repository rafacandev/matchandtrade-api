package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.*;
import com.matchandtrade.persistence.entity.Entity;
import com.matchandtrade.persistence.facade.QueryableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SearchService<E extends Entity> {

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private QueryableRepository<E> queryableRepository;

	public SearchResult<E> search(SearchCriteria searchCriteria, Class<? extends QueryBuilder> queryBuilderClass) {
		QueryBuilder queryBuilder = applicationContext.getBean(queryBuilderClass);
		return queryableRepository.query(searchCriteria, queryBuilder);
	}

}
