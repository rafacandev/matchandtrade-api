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
		TradeEntity.State result = null;
		switch (state) {
		case SUBMITTING_ITEMS:
			result = TradeEntity.State.SUBMITTING_ITEMS;
			break;
		case MATCHING_ITEMS:
			result = TradeEntity.State.MATCHING_ITEMS;
			break;
		case ITEMS_MATCHED:
			result = TradeEntity.State.ITEMS_MATCHED;
			break;
		case GENERATE_RESULTS:
			result = TradeEntity.State.GENERATE_RESULTS;
			break;
		case GENERATING_RESULTS:
			result = TradeEntity.State.GENERATING_RESULTS;
			break;
		case RESULTS_GENERATED:
			result = TradeEntity.State.RESULTS_GENERATED;
			break;
		case CANCELED:
			result = TradeEntity.State.CANCELED;
		default:
			break;
		}
		return result;
	}

	private static TradeJson.State buildState(TradeEntity.State state) {
		if (state == null) {
			return null;
		}
		TradeJson.State result = null;
		switch (state) {
		case SUBMITTING_ITEMS:
			result = TradeJson.State.SUBMITTING_ITEMS;
			break;
		case MATCHING_ITEMS:
			result = TradeJson.State.MATCHING_ITEMS;
			break;
		case ITEMS_MATCHED:
			result = TradeJson.State.ITEMS_MATCHED;
			break;
		case GENERATE_RESULTS:
			result = TradeJson.State.GENERATE_RESULTS;
			break;
		case GENERATING_RESULTS:
			result = TradeJson.State.GENERATING_RESULTS;
			break;
		case RESULTS_GENERATED:
			result = TradeJson.State.RESULTS_GENERATED;
			break;
		case CANCELED:
			result = TradeJson.State.CANCELED;
		default:
			break;
		}
		return result;
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
