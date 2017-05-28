package com.matchandtrade.test.random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.repository.TradeMembershipRepository;
import com.matchandtrade.repository.TradeRepository;

@Component
public class TradeMembershipRandom {
	
	@Autowired
	private TradeMembershipRepository tradeMembershipRepository;
	@Autowired
	private TradeRepository tradeRepository;
	
	public TradeMembershipEntity nextEntity(UserEntity tradeOwner) {
		TradeEntity tradeEntity = nextPersistedEntityWithoutOwner();
		TradeMembershipEntity result = new TradeMembershipEntity();
		result.setUser(tradeOwner);
		result.setTrade(tradeEntity);
		return result;
	}

	public TradeMembershipEntity nextPersistedEntity(UserEntity tradeOwner) {
		TradeEntity trade = nextPersistedEntityWithoutOwner();
		return nextPersistedEntity(trade, tradeOwner, TradeMembershipEntity.Type.OWNER);
	}

	public TradeMembershipEntity nextPersistedEntity(TradeEntity trade, UserEntity tradeOwner) {
		return nextPersistedEntity(trade, tradeOwner, TradeMembershipEntity.Type.OWNER);
	}

	public TradeMembershipEntity nextPersistedEntity(TradeEntity trade, UserEntity user, TradeMembershipEntity.Type membershipType) {
		TradeMembershipEntity result = new TradeMembershipEntity();
		result.setUser(user);
		result.setTrade(trade);
		result.setType(membershipType);
		tradeMembershipRepository.save(result);
		return result;
	}

	private TradeEntity nextPersistedEntityWithoutOwner() {
		TradeEntity result = TradeRandom.nextEntity();
		tradeRepository.save(result);
		return result;
	}

}