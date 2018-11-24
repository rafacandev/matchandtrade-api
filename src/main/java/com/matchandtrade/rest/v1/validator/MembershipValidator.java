package com.matchandtrade.rest.v1.validator;

import com.matchandtrade.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.MembershipService;
import com.matchandtrade.rest.service.TradeService;
import com.matchandtrade.rest.service.UserService;
import com.matchandtrade.rest.v1.json.MembershipJson;

@Component
public class MembershipValidator {

	@Autowired
	MembershipService membershipService;
	@Autowired
	TradeService tradeService;
	@Autowired
	UserService userService;
	
	/**
	 * <p>@{code HttpStatus.BAD_REQUEST, "Membership.userId must refer to an existing User"}</p>
	 * <p>@{code HttpStatus.BAD_REQUEST, "Membership.tradeId must refer to an existing Trade"}</p>
	 * <p>@{code HttpStatus.BAD_REQUEST, "Trade.State must be SUBMITTING_ARTICLES when creating a new Membership"}</p>
	 * <p>@{code HttpStatus.BAD_REQUEST, "Membership.tradeId and Membership.userId combined must be unique"}</p>
	 *
	 * @param membership to be validated
	 */
	public void validatePost(MembershipJson membership) {
		if (membership.getUserId() == null || userService.find(membership.getUserId()) == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Membership.userId must refer to an existing User");
		}

		TradeEntity trade = tradeService.find(membership.getTradeId());
		if (trade == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Membership.tradeId must refer to an existing Trade");
		} else if (trade.getState() != TradeEntity.State.SUBMITTING_ARTICLES) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Trade.State must be SUBMITTING_ARTICLES when creating a new Membership");
		}

		SearchResult<MembershipEntity> searchResult = membershipService.findByTradeIdUserIdType(membership.getTradeId(), membership.getUserId(), null, 1, 1);
		if (!searchResult.isEmpty()) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Membership.tradeId and Membership.userId combined must be unique");
		}
	}

	/**
	 * <p>{@code HttpStatus.NOT_FOUND, "Membership.membershipId was not found"}</p>
	 * <p>{@code HttpStatus.FORBIDDEN, "User.userId does not own Membership.membershipId"}</p>
	 *
	 * @param authenticatedUser
	 * @param membershipId
	 */
	public void validateDelete(UserEntity authenticatedUser, Integer membershipId) {
		MembershipEntity membership = membershipService.find(membershipId);
		if (membership == null) {
			throw new RestException(HttpStatus.NOT_FOUND, "Membership.membershipId was not found");
		}
		if (!authenticatedUser.getUserId().equals(membership.getUser().getUserId())) {
			throw new RestException(HttpStatus.FORBIDDEN, "User.userId does not own Membership.membershipId");
		}
	}

	public void validateGet(Integer pageNumber, Integer pageSize) {
		PaginationValidator.validatePageNumberAndPageSize(pageNumber, pageSize);
	}

	/**
	 * <p>{@code HttpStatus.NOT_FOUND, "Membership.membershipId was not found"}</p>
	 *
	 * @param membershipId
	 */
	public void validateGet(Integer membershipId) {
		MembershipEntity membership = membershipService.find(membershipId);
		if (membership == null) {
			throw new RestException(HttpStatus.NOT_FOUND, "Membership.membershipId was not found");
		}
	}

}