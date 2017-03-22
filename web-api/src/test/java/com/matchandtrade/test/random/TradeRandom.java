package com.matchandtrade.test.random;

import com.matchandtrade.rest.v1.json.TradeJson;

public class TradeRandom {
	
	public static TradeJson next() {
		TradeJson result = new TradeJson();
		result.setName(StringRandom.nextName());
		return result;
	}
	
	public static TradeJson next(Integer tradeId) {
		TradeJson result = next();
		result.setTradeId(tradeId);
		return result;
	}

}