package com.matchandtrade.test.random;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.MembershipRepositoryFacade;
import com.matchandtrade.rest.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MembershipRandom {
	
	@Autowired
	private MembershipRepositoryFacade membershipRepositoryFacade;
	@Autowired
	private MembershipService membershipService;
	@Autowired
	private TradeRandom tradeRandom;

	public MembershipEntity createPersistedEntity(UserEntity user) {
		TradeEntity trade = tradeRandom.createPersistedEntity(user);
		SearchResult<MembershipEntity> searchResult = membershipService.findByTradeIdUserIdType(
			trade.getTradeId(),
			user.getUserId(),
			MembershipEntity.Type.OWNER,
			1,
			1);
		return searchResult.getResultList().get(0);
	}

	public MembershipEntity createPersistedEntity(UserEntity user, TradeEntity trade) {
		return createPersistedEntity(user, trade, MembershipEntity.Type.OWNER);
	}

	public MembershipEntity createPersistedEntity(UserEntity user, TradeEntity trade, MembershipEntity.Type membershipType) {
		MembershipEntity result = new MembershipEntity();
		result.setUser(user);
		result.setTrade(trade);
		result.setType(membershipType);
		membershipRepositoryFacade.save(result);
		return result;
	}

}