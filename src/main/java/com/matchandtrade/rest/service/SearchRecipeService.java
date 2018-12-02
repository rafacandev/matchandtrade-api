package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ArticleRecipeQueryBuilder;
import com.matchandtrade.persistence.dto.Dto;
import com.matchandtrade.persistence.criteria.QueryableRepository;
import com.matchandtrade.rest.v1.json.search.SearchCriteriaJson;
import com.matchandtrade.rest.v1.transformer.SearchTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// TODO Integartion + Unit tests
@Component
public class SearchRecipeService {
	@Autowired
	private QueryableRepository<Dto> queryableDto;
	@Autowired
	private ArticleRecipeQueryBuilder articleRecipeQueryBuilder;

	public SearchResult<Dto> search(SearchCriteriaJson request, Integer pageNumber, Integer pageSize) {
		SearchCriteria searchCriteria = SearchTransformer.transform(request, pageNumber, pageSize);
		return queryableDto.query(searchCriteria, articleRecipeQueryBuilder, articleRecipeQueryBuilder.makeResultTransformer());
	}
}
