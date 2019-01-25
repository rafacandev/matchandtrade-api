package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ArticleNativeQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchRecipeService {
	@Autowired
	private ArticleNativeQueryRepository nativeRepository;

	public SearchResult search(SearchCriteria searchCriteria) {
		return nativeRepository.search(searchCriteria);
	}
}
