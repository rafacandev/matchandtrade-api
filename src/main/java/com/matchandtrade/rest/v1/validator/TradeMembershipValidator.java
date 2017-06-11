package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.facade.TradeMembershipRepositoryFacade;
import com.matchandtrade.persistence.facade.TradeRepositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.TradeMembershipJson;

@Component
public class TradeMembershipValidator {

	@Autowired
	private TradeMembershipRepositoryFacade tradeMembershipRepository;
	@Autowired
	private UserRepositoryFacade userRepository;
	@Autowired
	private TradeRepositoryFacade tradeRepository;
	
	/**
	 * {@code TradeMembership.tradeId} must be valid.
	 * {@code TradeMembership.userId} must be valid.
	 * The combination of {@code TradeMembership.tradeId} and {@code TradeMembership.userId} must be unique.
	 * 
	 * @param json to be validated
	 */
	public void validatePost(TradeMembershipJson json) {
		if (userRepository.get(json.getUserId()) == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "TradeMembership.userId must refer to an existing User.");
		}
		if (tradeRepository.get(json.getTradeId()) == null) {
			throw new RestException(HttpStatus.BAD_REQUEST, "TradeMembership.tradeId must refer to an existing Trade.");
		}
		SearchResult<TradeMembershipEntity> searchResult = tradeMembershipRepository.findByTradeIdAndUserId(json.getTradeId(), json.getUserId(), new Pagination(1,1));
		if (!searchResult.getResultList().isEmpty()) {
			throw new RestException(HttpStatus.BAD_REQUEST, "The combination of TradeMembership.tradeId and TradeMembership.userId must be unique.");
		}
	}

	public void validateDelete(Integer tradeMembershipId) {
		TradeMembershipEntity tm = tradeMembershipRepository.get(tradeMembershipId);
		if (tm == null) {
			throw new RestException(HttpStatus.NOT_FOUND);
		}
	}

}