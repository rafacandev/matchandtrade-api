package com.matchandtrade.rest.v1.transformer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.SearchResult;
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
	
	public static SearchResult<TradeJson> transform(SearchResult<TradeEntity> searchResult) {
		List<TradeJson> resultList = new ArrayList<>();
		for(TradeEntity e : searchResult.getResultList()) {
			resultList.add(transform(e));
		}
		return new SearchResult<>(resultList, searchResult.getPagination());
	}

}
