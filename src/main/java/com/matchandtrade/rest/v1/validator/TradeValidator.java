package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.repository.TradeRepository;
import com.matchandtrade.rest.v1.json.TradeJson;

@Component
public class TradeValidator {

	@Autowired
	private TradeRepository tradeRepository;
	
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
}
