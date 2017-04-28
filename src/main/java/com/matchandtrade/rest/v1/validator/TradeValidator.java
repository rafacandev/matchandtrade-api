package com.matchandtrade.rest.v1.validator;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.repository.TradeMembershipRepository;
import com.matchandtrade.repository.TradeRepository;
import com.matchandtrade.rest.v1.json.TradeJson;

@Component
public class TradeValidator {

	@Autowired
	private TradeRepository tradeRepository;
	@Autowired
	private TradeMembershipRepository tradeMembershipRepository;
	
	/**
	 * {@code TradeJson.name} is mandatory, unique and must contain at least 3 characters.
	 * @param userId
	 * @param json
	 */
	public void validatePost(TradeJson json) {
		String tradeJsonNameConstraint = "Trade.name is mandatory, unique and must contain at least 3 characters.";
		if (json.getName() == null || json.getName().length() < 3) {
			throw new ValidationException(ValidationException.ErrorType.MANDATORY_PARAMETER, tradeJsonNameConstraint);
		}
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination());
		searchCriteria.addCriterion(TradeEntity.Field.name, json.getName());
		SearchResult<TradeEntity> searchResult = tradeRepository.search(searchCriteria);
		if (searchResult.getResultList().size() > 0) {
			throw new ValidationException(ValidationException.ErrorType.UNIQUE_PARAMETER, tradeJsonNameConstraint);
		}
	}

	@Transactional
	public void validatePut(TradeJson json, UserEntity user) {
		if (json.getTradeId() == null) {
			throw new ValidationException(ValidationException.ErrorType.MANDATORY_PARAMETER, "Trade.tradeId is mandatory.");
		}
		
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination(1,1));
		searchCriteria.addCriterion(TradeMembershipEntity.Field.tradeId, json.getTradeId());
		searchCriteria.addCriterion(TradeMembershipEntity.Field.userId, user.getUserId());
		SearchResult<TradeMembershipEntity> searchResult = tradeMembershipRepository.search(searchCriteria);

		if (searchResult.getResultList().isEmpty()) {
			throw new ValidationException(ValidationException.ErrorType.INVALID_OPERATION, "Authenticated user is a member of Trade.tradeId: " + json.getTradeId());
		}
		
		TradeMembershipEntity tradeMembershipEntity = searchResult.getResultList().get(0);
		if (tradeMembershipEntity.getUser().getUserId() != user.getUserId()) {
			throw new ValidationException(ValidationException.ErrorType.INVALID_OPERATION, "Authenticated user is a member the owner of Trade.tradeId: " + json.getTradeId());
		}
		validatePost(json);
	}
	
	@Transactional
	public void validateDelete(Integer tradeId) {
		if (tradeId == null) {
			throw new ValidationException(ValidationException.ErrorType.MANDATORY_PARAMETER, "tradeId is mandatory.");
		}
		
		TradeEntity tradeEntity = tradeRepository.get(tradeId);
		if (tradeEntity == null) {
			throw new ValidationException(ValidationException.ErrorType.INVALID_OPERATION, "Did not find a Trade with tradeId: " + tradeId);
		}
	}

}
