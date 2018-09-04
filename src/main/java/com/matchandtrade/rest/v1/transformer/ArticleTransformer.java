package com.matchandtrade.rest.v1.transformer;

import java.util.ArrayList;
import java.util.List;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.rest.v1.json.ArticleJson;

public class ArticleTransformer {
	
	// Utility classes should not have public constructors
	private ArticleTransformer() {}

	public static ArticleEntity transform(ArticleJson json) {
		ArticleEntity result = new ArticleEntity();
		result.setName(json.getName());
		result.setArticleId(json.getArticleId());
		result.setDescription(json.getDescription());
		return result;
	}

	public static ArticleJson transform(ArticleEntity entity) {
		ArticleJson result = new ArticleJson();
		result.setName(entity.getName());
		result.setArticleId(entity.getArticleId());
		result.setDescription(entity.getDescription());
		return result;
	}

	public static SearchResult<ArticleJson> transform(SearchResult<ArticleEntity> searchResult) {
		List<ArticleJson> resultList = new ArrayList<>();
		for (ArticleEntity e : searchResult.getResultList()) {
			resultList.add(transform(e));
		}
		return new SearchResult<>(resultList, searchResult.getPagination());
	}

}
