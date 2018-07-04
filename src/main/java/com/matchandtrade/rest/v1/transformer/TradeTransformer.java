package com.matchandtrade.rest.v1.transformer;

import java.util.ArrayList;
import java.util.List;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeEntity.State;
import com.matchandtrade.rest.v1.json.TradeJson;

public class TradeTransformer {

	private TradeTransformer() {}

	private static State buildState(TradeJson.State state) {
		if (state == null) {
			return null;
		}
		return TradeEntity.State.valueOf(state.name());
	}

	private static TradeJson.State buildState(TradeEntity.State state) {
		if (state == null) {
			return null;
		}
		return TradeJson.State.valueOf(state.name());
	}
	
	public static TradeEntity transform(TradeJson json) {
		TradeEntity result;
		result = new TradeEntity();
		result.setDescription(json.getDescription());
		result.setName(json.getName());
		result.setTradeId(json.getTradeId());
		result.setState(buildState(json.getState()));
		return result;
	}

	public static TradeJson transform(TradeEntity entity) {
		if (entity == null) {
			return null;
		}
		TradeJson result = new TradeJson();
		result.setDescription(entity.getDescription());
		result.setName(entity.getName());
		result.setTradeId(entity.getTradeId());
		result.setState(buildState(entity.getState()));
		return result;
	}

	public static SearchResult<TradeJson> transform(SearchResult<TradeEntity> searchResult) {
		List<TradeJson> resultList = new ArrayList<>();
		for(TradeEntity e : searchResult.getResultList()) {
			resultList.add(transform(e));
		}
		return new SearchResult<>(resultList, searchResult.getPagination());
	}

}
