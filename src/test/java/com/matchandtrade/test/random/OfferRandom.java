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

	public static OfferJson nextJson(Integer offeredItemId, Integer wantedItemId) {
		OfferJson result = new OfferJson();
		result.setOfferedItemId(offeredItemId);
		result.setWantedItemId(wantedItemId);
		return result;
	}

	public OfferEntity nextPersistedEntity(Integer offeredItemId, Integer wantedItemId) {
		ItemEntity offeredItem = itemService.get(offeredItemId);
		ItemEntity wantedItem = itemService.get(wantedItemId);
		
		OfferEntity offer = new OfferEntity();
		offer.setOfferedItem(offeredItem);
		offer.setWantedItem(wantedItem);
		
		offerService.create(offer);
		return offer;
	}

}