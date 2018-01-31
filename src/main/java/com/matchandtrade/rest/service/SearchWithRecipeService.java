package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ItemRecipeQueryBuilder;
import com.matchandtrade.persistence.entity.Entity;
import com.matchandtrade.persistence.facade.QueryableRepository;

@Component
public class SearchWithRecipeService {

	@Autowired
	private QueryableRepository<Entity> queryableEntity;
	@Autowired
	private ItemRecipeQueryBuilder itemRecipeQueryBuilder;
	
	public SearchResult<Entity> itemRecipe(SearchCriteria searchCriteria) {
		return queryableEntity.query(searchCriteria, itemRecipeQueryBuilder, itemRecipeQueryBuilder.makeResultTransformer());
	}
	
}
