package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.TradeMembershipService;
import com.matchandtrade.rest.service.TradeService;
import com.matchandtrade.rest.service.UserService;
import com.matchandtrade.rest.v1.json.TradeMembershipJson;

@Component
public class TradeMembershipValidator {

	@Autowired
	private TradeMembershipService tradeMembershipService;
	@Autowired
	private TradeService tradeService;
	@Autowired
	private UserService userService;
	
	/**
	 * {@code TradeMembership.tradeId} must be valid.
	 * {@code TradeMembership.userId} must be valid.
	 * The combination of {@code TradeMembership.tradeId} and {@code TradeMembership.userId} must be unique.
	 * Users can subscribe only when {@code Trade.State=SUBMITTING_ITEMS}
	 * 
	 * @param json to be validated
	 */
	public void validatePost(TradeMembershipJson json) {
		if (userService.get(json.getUserId()) == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "TradeMembership.userId must refer to an existing User.");
		}
		
		TradeEntity trade = tradeService.get(json.getTradeId());
		if (trade == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "TradeMembership.tradeId must refer to an existing Trade.");
		} else if (trade.getState() != TradeEntity.State.SUBMITTING_ITEMS) {
			throw new RestException(HttpStatus.BAD_REQUEST, "Trade.State must be SUBMITTING_ITEMS when creating a new TradeMembership.");
		}
		
		SearchResult<TradeMembershipEntity> searchResult = tradeMembershipService.searchByTradeIdUserIdType(json.getTradeId(), json.getUserId(), null, 1, 1);
		if (!searchResult.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.BAD_REQUEST, "The combination of TradeMembership.tradeId and TradeMembership.userId must be unique.");
		}
	}

	public void validateDelete(Integer tradeMembershipId) {
		TradeMembershipEntity tm = tradeMembershipService.get(tradeMembershipId);
		if (tm == null) {
			throw new RestException(HttpStatus.NOT_FOUND);
		}
	}

	public void validateGet(Integer pageNumber, Integer pageSize) {
		PaginationValidator.validatePageNumberAndPageSize(pageNumber, pageSize);
	}

}