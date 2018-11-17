package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.rest.v1.json.TradeJson;

public class TradeTransformer extends Transformer<TradeEntity, TradeJson> {

	@Override
	public TradeEntity transform(TradeJson json) {
		TradeEntity result;
		result = new TradeEntity();
		result.setDescription(json.getDescription());
		result.setName(json.getName());
		result.setTradeId(json.getTradeId());
		result.setState(transformState(json.getState()));
		return result;
	}

	@Override
	public TradeJson transform(TradeEntity entity) {
		TradeJson result = new TradeJson();
		result.setDescription(entity.getDescription());
		result.setName(entity.getName());
		result.setTradeId(entity.getTradeId());
		result.setState(transformState(entity.getState()));
		return result;
	}

	private TradeJson.State transformState(TradeEntity.State state) {
		if (state == null) {
			return null;
		}
		return TradeJson.State.valueOf(state.name());
	}

	private TradeEntity.State transformState(TradeJson.State state) {
		if (state == null) {
			return null;
		}
		return TradeEntity.State.valueOf(state.name());
	}

}
