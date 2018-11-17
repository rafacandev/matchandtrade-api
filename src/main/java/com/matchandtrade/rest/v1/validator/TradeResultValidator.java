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
		TradeEntity trade = tradeService.find(tradeId);
		if (trade.getState() != TradeEntity.State.RESULTS_GENERATED) {
			throw new RestException(HttpStatus.BAD_REQUEST, "TradeResult is only availble when Trade.State is RESULTS_GENERATED.");
		}
		if (trade.getResult() == null) {
			throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "There is no results for Trade.tradeId: " + tradeId);
		}
	}

}
