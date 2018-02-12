package com.matchandtrade.rest.v1.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.service.TradeService;

@Component
public class TradeResultValidator {

	@Autowired
	private TradeService tradeService;

	public void validateGet(Integer tradeId) {
		TradeEntity trade = tradeService.get(tradeId);
		if (trade.getState() != TradeEntity.State.GENERATE_RESULTS
			&& trade.getState() != TradeEntity.State.GENERATING_RESULTS
			&& trade.getState() != TradeEntity.State.RESULTS_GENERATED) {
			throw new RestException(HttpStatus.BAD_REQUEST, "TradeResult is only availble when Trade.State is GENERATE_RESULTS, GENERATING_RESULTS, RESULTS_GENERATED.");
		}
	}

}
