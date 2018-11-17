package com.matchandtrade.rest.v1.validator;

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
	private MembershipService membershipService;
	@Autowired
	private TradeService tradeService;
	@Autowired
	private UserService userService;
	
	/**
	 * {@code Membership.tradeId} must be valid.
	 * {@code Membership.userId} must be valid.
	 * The combination of {@code Membership.tradeId} and {@code Membership.userId} must be unique.
	 * Users can subscribe only when {@code Trade.State=SUBMITTING_ARTICLES}
	 * 
	 * @param json to be validated
	 */
	public void validatePost(MembershipJson json) {
		if (userService.get(json.getUserId()) == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Membership.userId must refer to an existing User.");
		}
		
		TradeEntity trade = tradeService.get(json.getTradeId());
		if (trade == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Membership.tradeId must refer to an existing Trade.");
		} else if (trade.getState() != TradeEntity.State.SUBMITTING_ARTICLES) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Trade.State must be SUBMITTING_ARTICLES when creating a new Membership.");
		}
		
		SearchResult<MembershipEntity> searchResult = membershipService.searchByTradeIdUserIdType(json.getTradeId(), json.getUserId(), null, 1, 1);
		if (!searchResult.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.BAD_REQUEST, "The combination of Membership.tradeId and Membership.userId must be unique.");
		}
	}

	public void validateDelete(Integer membershipId) {
		MembershipEntity tm = membershipService.get(membershipId);
		if (tm == null) {
			throw new RestException(HttpStatus.NOT_FOUND, "Membership.membershipId was not found");
		}
	}

	public void validateGet(Integer pageNumber, Integer pageSize) {
		PaginationValidator.validatePageNumberAndPageSize(pageNumber, pageSize);
	}

	public void validateGet(Integer membershipId) {
		MembershipEntity membership = membershipService.get(membershipId);
		if (membership == null) {
			throw new RestException(HttpStatus.NOT_FOUND, "Membership.membershipId was not found");
		}
	}

}