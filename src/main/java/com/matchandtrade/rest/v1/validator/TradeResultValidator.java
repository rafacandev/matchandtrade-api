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
		if (trade.getState() != TradeEntity.State.MATCHING_ITEMS_ENDED
			&& trade.getState() != TradeEntity.State.GENERATING_TRADES_ENDED) {
			throw new RestException(HttpStatus.BAD_REQUEST, "TradeResult is only availble when Trade.State is MATCHING_ITEMS_ENDED or GENERATING_TRADES_ENDED");
		}
	}

}
