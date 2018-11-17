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
	private TradeTransformer tradeTransformer = new TradeTransformer();

	public TradeEntity createEntity() {
		return tradeTransformer.transform(createJson());
	}

	public TradeEntity createPersistedEntity(UserEntity owner) {
		TradeEntity result = createEntity();
		tradeService.create(result, owner);
		return result;
	}

	public static TradeJson createJson() {
		TradeJson result = new TradeJson();
		result.setName(StringRandom.nextName());
		result.setDescription(StringRandom.nextDescription());
		result.setState(TradeJson.State.SUBMITTING_ARTICLES);
		return result;
	}

}