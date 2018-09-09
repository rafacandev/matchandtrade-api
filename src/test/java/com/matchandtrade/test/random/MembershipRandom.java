package com.matchandtrade.test.random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.MembershipRepositoryFacade;
import com.matchandtrade.persistence.facade.TradeRepositoryFacade;

@Component
public class MembershipRandom {
	
	@Autowired
	private MembershipRepositoryFacade membershipRepository;
	@Autowired
	private TradeRepositoryFacade tradeRepository;
	
	public MembershipEntity nextEntity(UserEntity tradeOwner) {
		TradeEntity tradeEntity = nextPersistedEntityWithoutOwner();
		MembershipEntity result = new MembershipEntity();
		result.setUser(tradeOwner);
		result.setTrade(tradeEntity);
		return result;
	}

	public MembershipEntity nextPersistedEntity(UserEntity tradeOwner) {
		TradeEntity trade = nextPersistedEntityWithoutOwner();
		return nextPersistedEntity(trade, tradeOwner, MembershipEntity.Type.OWNER);
	}

	public MembershipEntity nextPersistedEntity(TradeEntity trade, UserEntity tradeOwner) {
		return nextPersistedEntity(trade, tradeOwner, MembershipEntity.Type.OWNER);
	}

	public MembershipEntity nextPersistedEntity(TradeEntity trade, UserEntity user, MembershipEntity.Type membershipType) {
		MembershipEntity result = new MembershipEntity();
		result.setUser(user);
		result.setTrade(trade);
		result.setType(membershipType);
		membershipRepository.save(result);
		return result;
	}

	private TradeEntity nextPersistedEntityWithoutOwner() {
		TradeEntity result = TradeRandom.nextEntity();
		tradeRepository.save(result);
		return result;
	}

}