package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.ItemService;
import com.matchandtrade.rest.service.OfferService;
import com.matchandtrade.rest.service.TradeMembershipService;
import com.matchandtrade.rest.service.UserService;
import com.matchandtrade.rest.v1.json.OfferJson;

@Component
public class OfferValidator {
	
	@Autowired
	private OfferService offerService;
	@Autowired
	private TradeMembershipService tradeMembershipService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private UserService userService;

	private static void tradeMembershipMustBelongToAuthenticatedUser(TradeMembershipEntity tradeMembership, Integer authenticatedUserId) {
		if (tradeMembership == null || !tradeMembership.getUser().getUserId().equals(authenticatedUserId)) {
			throw new RestException(HttpStatus.BAD_REQUEST, "TradeMembership.tradeMembershipId must belong to the authenticated User.");
		}
	}

	public void validateDelete(Integer tradeMembershipId, Integer offerId, Integer authenticatedUserId) {
		TradeMembershipEntity membership = tradeMembershipService.get(tradeMembershipId);
		tradeMembershipMustBelongToAuthenticatedUser(membership, authenticatedUserId);
		
		
		UserEntity offeredItemUser = userService.searchByOfferId(offerId);
		if (offeredItemUser == null || !offeredItemUser.getUserId().equals(authenticatedUserId)) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offerId must bellong to the offering User.userId.");
		}
	}

	public void validateGetAll(Integer tradeMembershipId, Integer offeredArticleId, Integer wantedArticleId,
			Integer pageNumber, Integer pageSize, Integer authenticatedUserId) {
		PaginationValidator.validatePageNumberAndPageSize(pageNumber, pageSize);	
		TradeMembershipEntity tradeMembership = tradeMembershipService.get(tradeMembershipId);
		tradeMembershipMustBelongToAuthenticatedUser(tradeMembership, authenticatedUserId);
	}

	public void validateGetById(Integer tradeMembershipId, Integer offerId, Integer authenticatedUserId) {
		TradeMembershipEntity tradeMembership = tradeMembershipService.get(tradeMembershipId);
		tradeMembershipMustBelongToAuthenticatedUser(tradeMembership, authenticatedUserId);
	}

	public void validatePost(Integer tradeMembershipId, OfferJson offer, Integer offeringUserId) {
		if (offer == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer is mandatory.");
		}

		if (offer.getOfferedArticleId() == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offeredArticleId is mandatory.");
		}

		if (offer.getWantedArticleId() == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.wantedArticleId is mandatory.");
		}
		
		if (offer.getOfferedArticleId().equals(offer.getWantedArticleId())) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offeredArticleId and Offer.wantedArticleId must differ.");
		}
		
		boolean itemsExist = itemService.exists(offer.getOfferedArticleId(), offer.getWantedArticleId());
		if (!itemsExist) {	
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offeredArticleId and Offer.wantedArticleId must belong to existing Articles.");
		}
		
		TradeMembershipEntity tradeMembership = tradeMembershipService.get(tradeMembershipId);
		if (!offeringUserId.equals(tradeMembership.getUser().getUserId())) {
			throw new RestException(HttpStatus.BAD_REQUEST, "TradeMembership must belong to the current authenticated User.");
		}
		
		UserEntity offeredItemUser = userService.searchByArticleId(offer.getOfferedArticleId());
		if (offeredItemUser == null || !offeredItemUser.getUserId().equals(offeringUserId)) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offeredArticleId must belong to the offering User.userId.");
		}
		
		boolean itemsAssociatedToSameTrade = offerService.areItemsAssociatedToSameTrade(offer.getOfferedArticleId(), offer.getWantedArticleId());
		if (!itemsAssociatedToSameTrade) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offeredArticleId and Offer.wantedArticleId must be associated to the same Trade.");
		}
	}

}
