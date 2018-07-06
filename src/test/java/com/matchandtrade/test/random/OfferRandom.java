package com.matchandtrade.test.random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.ItemEntity;
import com.matchandtrade.persistence.entity.OfferEntity;
import com.matchandtrade.rest.service.ItemService;
import com.matchandtrade.rest.service.OfferService;
import com.matchandtrade.rest.v1.json.OfferJson;

@Component
public class OfferRandom {

	@Autowired
	private ItemService itemService;
	@Autowired
	private OfferService offerService;

	public static OfferJson nextJson(Integer offeredArticleId, Integer wantedArticleId) {
		OfferJson result = new OfferJson();
		result.setOfferedArticleId(offeredArticleId);
		result.setWantedArticleId(wantedArticleId);
		return result;
	}

	public OfferEntity nextPersistedEntity(Integer tradeMembershipId, Integer offeredArticleId, Integer wantedArticleId) {
		ItemEntity offeredItem = itemService.get(offeredArticleId);
		ItemEntity wantedItem = itemService.get(wantedArticleId);
		
		OfferEntity offer = new OfferEntity();
		offer.setOfferedArticle(offeredItem);
		offer.setWantedArticle(wantedItem);
		
		offerService.create(tradeMembershipId, offer);
		return offer;
	}

}