package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.repository.TradeMembershipRepository;
import com.matchandtrade.repository.TradeRepository;
import com.matchandtrade.repository.UserRepository;
import com.matchandtrade.rest.v1.json.TradeMembershipJson;

@Component
public class TradeMembershipValidator {

	@Autowired
	private TradeMembershipRepository tradeMembershipRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TradeRepository tradeRepository;
	
	/**
	 * {@code TradeMembership.tradeId} must be valid.
	 * {@code TradeMembership.userId} must be valid.
	 * The combination of {@code TradeMembership.tradeId} and {@code TradeMembership.userId} must be unique.
	 * 
	 * @param TradeMembershipJson to be validated
	 */
	public void validatePost(TradeMembershipJson json) {
		if (userRepository.get(json.getUserId()) == null) {
			throw new ValidationException(ValidationException.ErrorType.INVALID_OPERATION, "TradeMembership.userId must be valid.");
		}
		if (tradeRepository.get(json.getTradeId()) == null) {
			throw new ValidationException(ValidationException.ErrorType.INVALID_OPERATION, "TradeMembership.tradeId must be valid.");
		}
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(1, 1));
		searchCriteria.addCriterion(TradeMembershipEntity.Field.tradeId, json.getTradeId());
		searchCriteria.addCriterion(TradeMembershipEntity.Field.userId, json.getUserId());
		SearchResult<TradeMembershipEntity> searchResult = tradeMembershipRepository.search(searchCriteria);
		if (searchResult.getResultList().size() > 0) {
			throw new ValidationException(ValidationException.ErrorType.INVALID_OPERATION, "The combination of TradeMembership.tradeId and TradeMembership.userId must be unique.");
		}
	}
}
