package com.matchandtrade.test.helper;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchHelper {

	@Autowired
	private MembershipService membershipService;

	public MembershipEntity findMembership(UserEntity user, TradeEntity trade) {
		SearchResult<MembershipEntity> searchResult = membershipService.findByTradeIdUserIdType(
			trade.getTradeId(),
			user.getUserId(),
			null,
			1,
			1);
		MembershipEntity persistedEntity = searchResult.getResultList().get(0);
		MembershipEntity result = new MembershipEntity();
		result.setMembershipId(persistedEntity.getMembershipId());
		result.setType(persistedEntity.getType());
		result.setUser(user);
		result.setTrade(trade);
		return result;
	}

}
