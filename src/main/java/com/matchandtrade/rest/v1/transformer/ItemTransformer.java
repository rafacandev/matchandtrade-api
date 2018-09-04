package com.matchandtrade.rest.v1.transformer;

import java.util.ArrayList;
import java.util.List;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.rest.v1.json.ItemJson;

public class ItemTransformer {
	
	// Utility classes should not have public constructors
	private ItemTransformer() {}

	public static ArticleEntity transform(ItemJson json) {
		ArticleEntity result = new ArticleEntity();
		result.setName(json.getName());
		result.setArticleId(json.getArticleId());
		result.setDescription(json.getDescription());
		return result;
	}

	public static ItemJson transform(ArticleEntity itemEntity) {
		ItemJson result = new ItemJson();
		result.setName(itemEntity.getName());
		result.setArticleId(itemEntity.getArticleId());
		result.setDescription(itemEntity.getDescription());
		return result;
	}

	public static SearchResult<ItemJson> transform(SearchResult<ArticleEntity> searchResult) {
		List<ItemJson> resultList = new ArrayList<>();
		for (ArticleEntity e : searchResult.getResultList()) {
			resultList.add(transform(e));
		}
		return new SearchResult<>(resultList, searchResult.getPagination());
	}

}
