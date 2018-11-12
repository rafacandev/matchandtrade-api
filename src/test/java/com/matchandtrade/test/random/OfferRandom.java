package com.matchandtrade.test.random;

import com.matchandtrade.persistence.facade.ArticleRepositoryFacade;
import com.matchandtrade.persistence.facade.MembershipRepositoryFacade;
import com.matchandtrade.rest.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.rest.v1.json.OfferJson;

@Component
public class OfferRandom {

	@Autowired
	private ArticleRepositoryFacade articleRepositoryFacade;
	@Autowired
	private MembershipRepositoryFacade membershipRepositoryFacade;
	@Autowired
	private OfferService offerService;

	public static OfferJson createJson(Integer offeredArticleId, Integer wantedArticleId) {
		OfferJson result = new OfferJson();
		result.setOfferedArticleId(offeredArticleId);
		result.setWantedArticleId(wantedArticleId);
		return result;
	}

	public OfferEntity createPersistedEntity(Integer membershipId, Integer offeredArticleId, Integer wantedArticleId) {
		ArticleEntity offeredArticle = articleRepositoryFacade.get(offeredArticleId);
		ArticleEntity wantedArticle = articleRepositoryFacade.get(wantedArticleId);

		OfferEntity offer = new OfferEntity();
		offer.setOfferedArticle(offeredArticle);
		offer.setWantedArticle(wantedArticle);
		
		offerService.create(membershipId, offer);
		return offer;
	}

}