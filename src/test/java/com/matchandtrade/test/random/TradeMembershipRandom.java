package com.matchandtrade.test.random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.repository.TradeMembershipRepository;

@Component
public class TradeMembershipRandom {
	
	@Autowired
	private TradeMembershipRepository tradeMembershipRepository;
	@Autowired
	private TradeRandom tradeRandom;
	
	public TradeMembershipEntity nextEntity(UserEntity tradeOwner) {
		TradeEntity tradeEntity = tradeRandom.nextPersistedEntity(tradeOwner);
		TradeMembershipEntity result = new TradeMembershipEntity();
		result.setUser(tradeOwner);
		result.setTrade(tradeEntity);
		return result;
	}
	
	public TradeMembershipEntity nextPersistedEntity(UserEntity tradeOwner) {
		TradeEntity tradeEntity = tradeRandom.nextPersistedEntity(tradeOwner);
		TradeMembershipEntity result = new TradeMembershipEntity();
		result.setUser(tradeOwner);
		result.setTrade(tradeEntity);
		tradeMembershipRepository.save(result);
		return result;
	}

}