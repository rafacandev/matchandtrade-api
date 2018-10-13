package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.rest.service.MembershipArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.OfferService;
import com.matchandtrade.rest.service.MembershipService;
import com.matchandtrade.rest.service.UserService;
import com.matchandtrade.rest.v1.json.OfferJson;

@Component
public class OfferValidator {
	
	@Autowired
	private OfferService offerService;
	@Autowired
	private MembershipService membershipService;
	@Autowired
	private MembershipArticleService membershipArticleService;
	@Autowired
	private UserService userService;

	private static void membershipMustBelongToAuthenticatedUser(MembershipEntity membership, Integer authenticatedUserId) {
		if (membership == null || !membership.getUser().getUserId().equals(authenticatedUserId)) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Membership.membershipId must belong to the authenticated User.");
		}
	}

	public void validateDelete(Integer membershipId, Integer offerId, Integer authenticatedUserId) {
		MembershipEntity membership = membershipService.get(membershipId);
		membershipMustBelongToAuthenticatedUser(membership, authenticatedUserId);
		
		
		UserEntity offeredArticleUser = userService.searchByOfferId(offerId);
		if (offeredArticleUser == null || !offeredArticleUser.getUserId().equals(authenticatedUserId)) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offerId must bellong to the offering User.userId.");
		}
	}

	public void validateGetAll(Integer membershipId, Integer offeredArticleId, Integer wantedArticleId,
			Integer pageNumber, Integer pageSize, Integer authenticatedUserId) {
		PaginationValidator.validatePageNumberAndPageSize(pageNumber, pageSize);	
		MembershipEntity membership = membershipService.get(membershipId);
		membershipMustBelongToAuthenticatedUser(membership, authenticatedUserId);
	}

	public void validateGetById(Integer membershipId, Integer offerId, Integer authenticatedUserId) {
		MembershipEntity membership = membershipService.get(membershipId);
		membershipMustBelongToAuthenticatedUser(membership, authenticatedUserId);
	}

	public void validatePost(Integer membershipId, OfferJson offer, Integer offeringUserId) {
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
		
		boolean articlesExist = membershipArticleService.exists(offer.getOfferedArticleId(), offer.getWantedArticleId());
		if (!articlesExist) {	
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offeredArticleId and Offer.wantedArticleId must belong to existing Articles.");
		}
		
		MembershipEntity membership = membershipService.get(membershipId);
		if (!offeringUserId.equals(membership.getUser().getUserId())) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Membership must belong to the current authenticated User.");
		}
		
		UserEntity offeredArticleUser = userService.searchByArticleId(offer.getOfferedArticleId());
		if (offeredArticleUser == null || !offeredArticleUser.getUserId().equals(offeringUserId)) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offeredArticleId must belong to the offering User.userId.");
		}
		
		boolean articlesAssociatedToSameTrade = offerService.areArticlesAssociatedToSameTrade(offer.getOfferedArticleId(), offer.getWantedArticleId());
		if (!articlesAssociatedToSameTrade) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offeredArticleId and Offer.wantedArticleId must be associated to the same Trade.");
		}
	}

}
