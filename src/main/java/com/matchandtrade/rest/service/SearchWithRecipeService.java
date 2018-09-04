package com.matchandtrade.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ArticleRecipeQueryBuilder;
import com.matchandtrade.persistence.entity.Entity;
import com.matchandtrade.persistence.facade.QueryableRepository;

@Component
public class SearchWithRecipeService {

	@Autowired
	private QueryableRepository<Entity> queryableEntity;
	@Autowired
	private ArticleRecipeQueryBuilder articleRecipeQueryBuilder;
	
	public SearchResult<Entity> articleRecipe(SearchCriteria searchCriteria) {
		return queryableEntity.query(searchCriteria, articleRecipeQueryBuilder, articleRecipeQueryBuilder.makeResultTransformer());
	}
	
}
