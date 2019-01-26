package com.matchandtrade.rest.service;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ArticleNativeQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchRecipeService {
	public enum Field implements com.matchandtrade.persistence.common.Field {
		ARTICLE_ID("Article.articleId"),
		USER_ID("User.userId"),
		TRADE_ID("Trade.tradeId");
		private String alias;
		Field(String alias) { this.alias = alias; }
		@Override
		public String alias() { return alias; }
	}

	@Autowired
	private ArticleNativeQueryRepository nativeRepository;

	public SearchResult search(SearchCriteria searchCriteria) {
		return nativeRepository.search(searchCriteria);
	}
}
