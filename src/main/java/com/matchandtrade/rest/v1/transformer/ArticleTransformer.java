package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.rest.v1.json.ArticleJson;

public class ArticleTransformer extends Transformer<ArticleEntity, ArticleJson> {

	@Override
	public ArticleJson transform(ArticleEntity entity) {
		ArticleJson result = new ArticleJson();
		result.setName(entity.getName());
		result.setArticleId(entity.getArticleId());
		result.setDescription(entity.getDescription());
		return result;
	}

	@Override
	public ArticleEntity transform(ArticleJson json) {
		ArticleEntity result = new ArticleEntity();
		result.setName(json.getName());
		result.setArticleId(json.getArticleId());
		result.setDescription(json.getDescription());
		return result;
	}

}
