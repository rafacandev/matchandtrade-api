package com.matchandtrade.test.helper;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ArticleQueryBuilder;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.rest.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchHelper {

	@Autowired
	private SearchService<ArticleEntity> searchServiceArticle;

	public boolean membershipContainsArticle(Integer membershipId, Integer articleId) {
		SearchCriteria criteria = new SearchCriteria(new Pagination());
		criteria.addCriterion(ArticleQueryBuilder.Field.MEMBERSHIP_ID, membershipId);
		criteria.addCriterion(ArticleQueryBuilder.Field.ARTICLE_ID, articleId);
		SearchResult<ArticleEntity> searchResult = searchServiceArticle.searchCake(criteria, ArticleQueryBuilder.class);
		return searchResult.getResultList().size() > 0;
	}

}
