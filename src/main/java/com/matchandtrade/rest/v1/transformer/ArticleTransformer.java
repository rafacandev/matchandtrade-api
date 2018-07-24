package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.rest.v1.json.ArticleJson;
import com.matchandtrade.rest.v1.json.ItemJson;

public class ArticleTransformer {
	
	public static ArticleEntity transform(ArticleJson json) {
		if (json instanceof ItemJson) {
			ItemJson itemJson = (ItemJson) json;
			return ItemTransformer.transform(itemJson);
		}
		throw new IllegalArgumentException("ArticleJson must be an instance of ItemJson.");
	}

	public static ArticleJson transform(ArticleEntity entity) {
		if (entity instanceof ItemEntity) {
			ItemEntity itemEntity = (ItemEntity) entity;
			return ItemTransformer.transform(itemEntity);
		}
		throw new IllegalArgumentException("ArticleEntity must be an instance of ItemEntity.");
	}

}
