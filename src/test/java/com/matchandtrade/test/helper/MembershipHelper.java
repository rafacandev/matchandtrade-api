package com.matchandtrade.test.helper;

import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static com.matchandtrade.persistence.entity.MembershipEntity.Type.MEMBER;
import static com.matchandtrade.persistence.entity.MembershipEntity.Type.OWNER;

@Component
@Transactional
@Commit
public class MembershipHelper {
	
	@Autowired
	private MembershipService membershipService;
	@Autowired
	private TradeHelper tradeHelper;

	public MembershipEntity createPersistedEntity(UserEntity user) {
		TradeEntity trade = tradeHelper.createPersistedEntity(user);
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