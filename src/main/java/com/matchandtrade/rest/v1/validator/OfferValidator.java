package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.ArticleEntity;
import com.matchandtrade.rest.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.OfferJson;

@Component
public class OfferValidator {

	@Autowired
	ArticleService articleService;
	@Autowired
	private OfferService offerService;
	@Autowired
	MembershipService membershipService;
	@Autowired
	TradeService tradeService;
	@Autowired
	UserService userService;

	private static void membershipMustBelongToAuthenticatedUser(MembershipEntity membership, Integer authenticatedUserId) {
		if (membership == null || !membership.getUser().getUserId().equals(authenticatedUserId)) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Membership.membershipId must belong to the authenticated User.");
		}
	}

	public void validateDelete(Integer membershipId, Integer offerId, Integer authenticatedUserId) {
		MembershipEntity membership = membershipService.find(membershipId);
		membershipMustBelongToAuthenticatedUser(membership, authenticatedUserId);
		UserEntity offeredArticleUser = userService.searchByOfferId(offerId);
		if (offeredArticleUser == null || !offeredArticleUser.getUserId().equals(authenticatedUserId)) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offerId must bellong to the offering User.userId.");
		}
	}

	public void validateGetAll(Integer membershipId, Integer pageNumber, Integer pageSize, Integer authenticatedUserId) {
		PaginationValidator.validatePageNumberAndPageSize(pageNumber, pageSize);
		MembershipEntity membership = membershipService.find(membershipId);
		membershipMustBelongToAuthenticatedUser(membership, authenticatedUserId);
	}

	public void validateGetById(Integer membershipId, Integer authenticatedUserId) {
		MembershipEntity membership = membershipService.find(membershipId);
		membershipMustBelongToAuthenticatedUser(membership, authenticatedUserId);
	}

	public void validatePost(Integer membershipId, OfferJson offer, Integer authenticatedUserId) {
		if (offer.getOfferedArticleId() == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offeredArticleId is mandatory");
		}

		if (offer.getWantedArticleId() == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.wantedArticleId is mandatory");
		}

		if (offer.getOfferedArticleId().equals(offer.getWantedArticleId())) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offeredArticleId and Offer.wantedArticleId must differ");
		}

		ArticleEntity offeredArticle = articleService.find(offer.getOfferedArticleId());
		if (offeredArticle == null) {
			throw new RestException(HttpStatus.NOT_FOUND, "Offer.offeredArticleId was not found");
		}

		ArticleEntity wantedArticle = articleService.find(offer.getWantedArticleId());
		if (wantedArticle == null) {
			throw new RestException(HttpStatus.NOT_FOUND, "Offer.wantedArticleId was not found");
		}

		MembershipEntity membership = membershipService.find(membershipId);
		if (!authenticatedUserId.equals(membership.getUser().getUserId())) {
			throw new RestException(HttpStatus.FORBIDDEN, "User.userId does not own Membership.membershipId");
		}

		UserEntity userOfOfferedArticle = userService.findByArticleId(offer.getOfferedArticleId());
		if (!userOfOfferedArticle.getUserId().equals(authenticatedUserId)) {
			throw new RestException(HttpStatus.FORBIDDEN, "User.userId does not own Offer.offeredArticleId");
		}

		boolean areArticlesInSameTrade = tradeService.areArticlesInSameTrade(membership.getTrade().getTradeId(), offer.getOfferedArticleId(), offer.getWantedArticleId());
		if (!areArticlesInSameTrade) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Offer.offeredArticleId and Offer.wantedArticleId must be associated to the same Trade.tradeId");
		}
	}

}
