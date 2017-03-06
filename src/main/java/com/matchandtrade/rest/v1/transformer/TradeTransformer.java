package com.matchandtrade.rest.v1.transformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.model.TradeModel;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.rest.v1.json.TradeJson;

@Component
public class TradeTransformer {

	@Autowired
	private TradeModel tradeModel;

	public TradeEntity transform(TradeJson json, boolean loadEntity) {
		TradeEntity result;
		if (loadEntity) {
			result = tradeModel.get(json.getTradeId());
		} else {
			result = new TradeEntity();
		}
		result.setName(json.getName());
		result.setTradeId(json.getTradeId());
		return result;
	}

	public TradeEntity transform(TradeJson json) {
		return transform(json, false);
	}

	public static TradeJson transform(TradeEntity entity) {
		if (entity == null) {
			return null;
		}
		TradeJson result = new TradeJson();
		result.setName(entity.getName());
		result.setTradeId(entity.getTradeId());
		return result;
	}

}
