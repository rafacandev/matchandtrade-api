package com.matchandtrade.test.helper;

import com.matchandtrade.test.StringRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.service.TradeService;
import com.matchandtrade.rest.v1.json.TradeJson;
import com.matchandtrade.rest.v1.transformer.TradeTransformer;

@Component
public class TradeHelper {

	@Autowired
	private TradeService tradeService;
	private TradeTransformer tradeTransformer = new TradeTransformer();
	@Autowired
	private UserHelper userHelper;

	public TradeEntity createPersistedEntity() {
		return createPersistedEntity(userHelper.createPersistedEntity());
	}

	public TradeEntity createPersistedEntity(UserEntity owner) {
		TradeEntity result = createRandomEntity();
		tradeService.create(result, owner);
		return result;
	}

	public static TradeJson createRandomJson() {
		TradeJson result = new TradeJson();
		result.setName(StringRandom.nextName() + System.currentTimeMillis());
		result.setDescription(StringRandom.nextDescription());
		result.setState(TradeJson.State.SUBMITTING_ARTICLES);
		return result;
	}

	public TradeEntity createRandomEntity() {
		return tradeTransformer.transform(createRandomJson());
	}

}