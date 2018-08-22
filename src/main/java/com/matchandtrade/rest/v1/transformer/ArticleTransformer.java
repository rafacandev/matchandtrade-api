package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.json.ItemJson;

public class ArticleTransformer {
	
	// Utility classes should not have public constructors
	private ArticleTransformer() {}
	
	public static ArticleEntity transform(ArticleJson json) {
		if (json instanceof ItemJson) {
			ItemJson item = (ItemJson) json;
			return ItemTransformer.transform(item);
		} else {
			ArticleEntity entity = new ArticleEntity();
			entity.setArticleId(json.getArticleId());
			entity.setName(json.getName());
			return entity;
		}
	}

	public static ArticleJson transform(ArticleEntity entity) {
		if (entity instanceof ItemEntity) {
			ItemEntity item = (ItemEntity) entity;
			return ItemTransformer.transform(item);
		} else {
			ArticleJson json = new ArticleJson();
			json.setArticleId(entity.getArticleId());
			json.setName(entity.getName());
			return json;
		}
	}

}
