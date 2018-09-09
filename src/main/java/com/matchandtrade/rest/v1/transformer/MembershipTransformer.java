package com.matchandtrade.rest.v1.transformer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.facade.TradeRepositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.v1.json.MembershipJson;
import com.matchandtrade.rest.v1.json.MembershipJson.Type;

@Component
public class MembershipTransformer {

	@Autowired
	private UserRepositoryFacade userRepository;
	@Autowired
	private TradeRepositoryFacade tradeRepository;

	public MembershipEntity transform(MembershipJson json) {
		MembershipEntity result;
		result = new MembershipEntity();
		result.setMembershipId(json.getMembershipId());
		result.setTrade(tradeRepository.get(json.getTradeId()));
		result.setUser(userRepository.get(json.getUserId()));
		return result;
	}

	public static MembershipJson transform(MembershipEntity entity) {
		if (entity == null) {
			return null;
		}
		MembershipJson result = new MembershipJson();
		result.setMembershipId(entity.getMembershipId());
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
	
	private static Type buildType(MembershipEntity.Type type) {
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

	public static SearchResult<MembershipJson> transform(SearchResult<MembershipEntity> searchResult) {
		List<MembershipJson> resultList = new ArrayList<>();
		for(MembershipEntity e : searchResult.getResultList()) {
			resultList.add(transform(e));
		}
		return new SearchResult<>(resultList, searchResult.getPagination());
	}
}
