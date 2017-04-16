package com.matchandtrade.rest.v1.transformer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.repository.TradeMembershipRepository;
import com.matchandtrade.repository.TradeRepository;
import com.matchandtrade.repository.UserRepository;
import com.matchandtrade.rest.v1.json.TradeMembershipJson;

@Component
public class TradeMembershipTransformer {

	@Autowired
	private TradeMembershipRepository tradeMembershipRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TradeRepository tradeRepository;

	public TradeMembershipEntity transform(TradeMembershipJson json, boolean loadEntity) {
		TradeMembershipEntity result;
		if (loadEntity) {
			result = tradeMembershipRepository.get(json.getTradeMembershipId());
		} else {
			result = new TradeMembershipEntity();
		}
		result.setTradeMembershipId(json.getTradeMembershipId());
		result.setTrade(tradeRepository.get(json.getTradeId()));
		result.setUser(userRepository.get(json.getUserId()));
		return result;
	}

	public TradeMembershipEntity transform(TradeMembershipJson json) {
		return transform(json, false);
	}

	public static TradeMembershipJson transform(TradeMembershipEntity entity) {
		if (entity == null) {
			return null;
		}
		TradeMembershipJson result = new TradeMembershipJson();
		result.setTradeMembershipId(entity.getTradeMembershipId());
		if (entity.getTrade() != null) {
			result.setTradeId(entity.getTrade().getTradeId());
		} else {
			result.setTradeId(null);
		}
		if (entity.getUser() != null) {
			result.setUserId(entity.getUser().getUserId());
		} else {
			result.setUserId(null);
		}
		return result;
	}
	
	public static SearchResult<TradeMembershipJson> transform(SearchResult<TradeMembershipEntity> searchResult) {
		List<TradeMembershipJson> resultList = new ArrayList<>();
		for(TradeMembershipEntity e : searchResult.getResultList()) {
			resultList.add(transform(e));
		}
		return new SearchResult<>(resultList, searchResult.getPagination());
	}
}
