package com.matchandtrade.test.helper;

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
	@Autowired
	private UserRandom userRandom;

	public static TradeJson createJson() {
		TradeJson result = new TradeJson();
		result.setName(StringRandom.nextName() + System.currentTimeMillis());
		result.setDescription(StringRandom.nextDescription());
		result.setState(TradeJson.State.SUBMITTING_ARTICLES);
		return result;
	}

	public TradeEntity createEntity() {
		return tradeTransformer.transform(createJson());
	}

	public TradeEntity createPersistedEntity() {
		return createPersistedEntity(userRandom.createPersistedEntity());
	}

	public TradeEntity createPersistedEntity(UserEntity owner) {
		TradeEntity result = createEntity();
		tradeService.create(result, owner);
		return result;
	}

}