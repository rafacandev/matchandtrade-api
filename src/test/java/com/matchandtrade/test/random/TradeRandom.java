package com.matchandtrade.test.random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.service.TradeService;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.rest.v1.transformer.TradeTransformer;

@Component
public class TradeRandom {

	@Autowired
	private TradeService tradeService;

	public static TradeEntity nextEntity() {
		return TradeTransformer.transform(nextJson());
	}
	
	public static TradeJson nextJson() {
		TradeJson result = new TradeJson();
		result.setName(StringRandom.nextName());
		result.setDescription(StringRandom.nextDescription());
		result.setState(TradeJson.State.SUBMITTING_ARTICLES);
		return result;
	}

	public TradeEntity nextPersistedEntity(UserEntity tradeOwner) {
		TradeEntity result = nextEntity();
		tradeService.create(result, tradeOwner);
		return result;
	}

}