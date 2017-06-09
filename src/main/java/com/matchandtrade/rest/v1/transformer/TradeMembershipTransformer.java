package com.matchandtrade.rest.v1.transformer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.facade.TradeRepositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.v1.json.TradeMembershipJson;
import com.matchandtrade.rest.v1.json.TradeMembershipJson.Type;

@Component
public class TradeMembershipTransformer {

	@Autowired
	private UserRepositoryFacade userRepository;
	@Autowired
	private TradeRepositoryFacade tradeRepository;

	public TradeMembershipEntity transform(TradeMembershipJson json) {
		TradeMembershipEntity result;
		result = new TradeMembershipEntity();
		result.setTradeMembershipId(json.getTradeMembershipId());
		result.setTrade(tradeRepository.get(json.getTradeId()));
		result.setUser(userRepository.get(json.getUserId()));
		return result;
	}

	public static TradeMembershipJson transform(TradeMembershipEntity entity) {
		if (entity == null) {
			return null;
		}
		TradeMembershipJson result = new TradeMembershipJson();
		result.setTradeMembershipId(entity.getTradeMembershipId());
		result.setType(buildType(entity.getType()));
		if (entity.getTrade() == null) {
			result.setTradeId(null);
		} else {
			result.setTradeId(entity.getTrade().getTradeId());
		}
		if (entity.getUser() == null) {
			result.setUserId(null);
		} else {
			result.setUserId(entity.getUser().getUserId());
		}
		return result;
	}
	
	private static Type buildType(TradeMembershipEntity.Type type) {
		switch (type) {
		case MEMBER:
			return Type.MEMBER;
		case OWNER:
			return Type.OWNER;
		default:
			break;
		}
		return null;
	}

	public static SearchResult<TradeMembershipJson> transform(SearchResult<TradeMembershipEntity> searchResult) {
		List<TradeMembershipJson> resultList = new ArrayList<>();
		for(TradeMembershipEntity e : searchResult.getResultList()) {
			resultList.add(transform(e));
		}
		return new SearchResult<>(resultList, searchResult.getPagination());
	}
}
