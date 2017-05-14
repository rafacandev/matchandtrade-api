package com.matchandtrade.test.random;

import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.rest.v1.transformer.TradeTransformer;

@Component
public class TradeRandom {
	
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
		return TradeTransformer.transform(nextJson());
	}

}