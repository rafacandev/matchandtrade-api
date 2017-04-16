package com.matchandtrade.test.random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.rest.v1.transformer.TradeTransformer;

@Component
public class TradeRandom {
	
	@Autowired
	private TradeTransformer tradeTransformer;
	
	public static TradeJson nextJson() {
		TradeJson result = new TradeJson();
		result.setName(StringRandom.nextName());
		return result;
	}
	
	// TODO remove this
	public static TradeJson next(Integer tradeId) {
		TradeJson result = nextJson();
		result.setTradeId(tradeId);
		return result;
	}
	
	public TradeEntity nextEntity() {
		return tradeTransformer.transform(nextJson());
	}

}