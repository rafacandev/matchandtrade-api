package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.persistence.repository.ArticleRepository;
import com.matchandtrade.rest.v1.json.OfferJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OfferTransformer extends Transformer<OfferEntity, OfferJson> {
	
	@Autowired
	private ArticleRepository articleRepository;

	@Override
	public OfferJson transform(OfferEntity entity) {
		OfferJson result = new OfferJson();
		result.setOfferId(entity.getOfferId());
		result.setOfferedArticleId(entity.getOfferedArticle().getArticleId());
		result.setWantedArticleId(entity.getWantedArticle().getArticleId());
		return result;
	}

	@Override
	public OfferEntity transform(OfferJson json) {
		OfferEntity result = new OfferEntity();
		result.setOfferId(json.getOfferId());
		result.setOfferedArticle(articleRepository.findOne(json.getOfferedArticleId()));
		result.setWantedArticle(articleRepository.findOne(json.getWantedArticleId()));
		return result;
	}

}
