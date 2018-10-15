package com.matchandtrade.test.random;

import com.matchandtrade.rest.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.rest.service.OfferService;
import com.matchandtrade.rest.v1.json.OfferJson;

@Component
public class OfferRandom {

	@Autowired
	private ArticleService articleService;
	@Autowired
	private OfferService offerService;

	public static OfferJson nextJson(Integer offeredArticleId, Integer wantedArticleId) {
		OfferJson result = new OfferJson();
		result.setOfferedArticleId(offeredArticleId);
		result.setWantedArticleId(wantedArticleId);
		return result;
	}

	public OfferEntity nextPersistedEntity(Integer membershipId, Integer offeredArticleId, Integer wantedArticleId) {
		ArticleEntity offeredArticle = articleService.get(offeredArticleId);
		ArticleEntity wantedArticle = articleService.get(wantedArticleId);
		
		OfferEntity offer = new OfferEntity();
		offer.setOfferedArticle(offeredArticle);
		offer.setWantedArticle(wantedArticle);
		
		offerService.create(membershipId, offer);
		return offer;
	}

}