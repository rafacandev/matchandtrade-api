package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.ItemService;
import com.matchandtrade.rest.service.OfferService;
import com.matchandtrade.rest.service.UserService;
import com.matchandtrade.rest.v1.json.OfferJson;

@Component
public class OfferValidator {
	
	@Autowired
	private OfferService offerService;
	
	@Autowired
	private ItemService itemService;

	@Autowired
	private UserService userService;
	
	public void validatePost(OfferJson offer, Integer offeringUserId) {
		if (offer == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer is mandatory.");
		}

		if (offer.getOfferedItemId() == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offeredItemId is mandatory.");
		}

		if (offer.getWantedItemId() == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.wantedItemId is mandatory.");
		}
		
		if (offer.getOfferedItemId().equals(offer.getWantedItemId())) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offeredItemId and Offer.wantedItemId must differ.");
		}
		
		boolean itemsExist = itemService.exists(offer.getOfferedItemId(), offer.getWantedItemId());
		if (!itemsExist) {	
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offeredItemId and Offer.wantedItemId must belong to existing Items.");
		}
		
		UserEntity offeredItemUser = userService.searchByItemId(offer.getOfferedItemId());
		if (offeredItemUser == null || !offeredItemUser.getUserId().equals(offeringUserId)) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offeredItemId must belong to the offering User.userId.");
		}
		
		boolean itemsAssociatedToSameTrade = offerService.areItemsAssociatedToSameTrade(offer.getOfferedItemId(), offer.getWantedItemId());
		if (!itemsAssociatedToSameTrade) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offeredItemId and Offer.wantedItemId must be associated to the same Trade.");
		}
	}

	public void validateDelete(Integer offerId, Integer userId) {
		UserEntity offeredItemUser = userService.searchByOfferId(offerId);
		if (offeredItemUser == null || !offeredItemUser.getUserId().equals(userId)) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offerId must bellong to the offering User.userId.");
		}
		
	}

}
