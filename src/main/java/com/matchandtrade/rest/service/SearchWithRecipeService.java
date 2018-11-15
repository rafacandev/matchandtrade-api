package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ArticleRecipeQueryBuilder;
import com.matchandtrade.persistence.entity.Entity;
import com.matchandtrade.persistence.facade.QueryableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// TODO Integartion + Unit tests
@Component
public class SearchWithRecipeService {

	@Autowired
	private QueryableRepository<Entity> queryableEntity;
	@Autowired
	private ArticleRecipeQueryBuilder articleRecipeQueryBuilder;

	// TODO: Can se just use the SearchService here as everybody else?
	public SearchResult<Entity> articleRecipe(SearchCriteria searchCriteria) {
		return queryableEntity.query(searchCriteria, articleRecipeQueryBuilder, articleRecipeQueryBuilder.makeResultTransformer());
	}
	
}
