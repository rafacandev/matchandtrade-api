package com.matchandtrade.test.helper;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.MembershipRepositoryFacade;
import com.matchandtrade.rest.service.MembershipService;
import com.matchandtrade.rest.v1.transformer.MembershipTransformer;
import com.matchandtrade.util.JsonUtil;
import com.mchange.v2.beans.BeansUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.matchandtrade.persistence.entity.MembershipEntity.Type.MEMBER;
import static com.matchandtrade.persistence.entity.MembershipEntity.Type.OWNER;

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
			OWNER,
			1,
			1);
		return searchResult.getResultList().get(0);
	}

	public MembershipEntity subscribeUserToTrade(UserEntity user, TradeEntity trade) {
		MembershipEntity result = new MembershipEntity();
		result.setUser(user);
		result.setTrade(trade);
		result.setType(MEMBER);
		membershipService.create(result);
		return result;
	}
}