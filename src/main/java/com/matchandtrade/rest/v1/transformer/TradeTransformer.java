package com.matchandtrade.rest.v1.transformer;

import java.util.ArrayList;
import java.util.List;

import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.rest.v1.json.TradeJson;

public class TradeTransformer {

	private TradeTransformer() {}
	
	public static TradeEntity transform(TradeJson json) {
		TradeEntity result;
		result = new TradeEntity();
		result.setName(json.getName());
		result.setTradeId(json.getTradeId());
		return result;
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
	
	public static SearchResult<TradeJson> transform(SearchResult<TradeEntity> searchResult) {
		List<TradeJson> resultList = new ArrayList<>();
		for(TradeEntity e : searchResult.getResultList()) {
			resultList.add(transform(e));
		}
		return new SearchResult<>(resultList, searchResult.getPagination());
	}

}
