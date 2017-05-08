package com.matchandtrade.test.random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.repository.TradeRepository;
import com.matchandtrade.repository.UserRepository;
import com.matchandtrade.rest.v1.json.TradeMembershipJson;

@Component
public class TradeMembershipRandom {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TradeRandom tradeRandom;
	@Autowired
	private TradeRepository tradeRepository;
	
	public TradeMembershipJson nextJson() {
		UserEntity userEntity = UserRandom.nextEntity();
		userRepository.save(userEntity);
		TradeEntity tradeEntity = tradeRandom.nextEntity();
		tradeRepository.save(tradeEntity);
		TradeMembershipJson result = new TradeMembershipJson();
		result.setTradeId(tradeEntity.getTradeId());
		result.setUserId(userEntity.getUserId());
		return result;
	}
	
	public TradeMembershipEntity nextEntity() {
		UserEntity userEntity = UserRandom.nextEntity();
		userRepository.save(userEntity);
		TradeEntity tradeEntity = tradeRandom.nextEntity();
		tradeRepository.save(tradeEntity);
		TradeMembershipEntity result = new TradeMembershipEntity();
		result.setTrade(tradeEntity);
		result.setUser(userEntity);
		return result;
	}
	
	public TradeMembershipEntity nextEntity(UserEntity user) {
		TradeEntity tradeEntity = tradeRandom.nextEntity();
		tradeRepository.save(tradeEntity);
		TradeMembershipEntity result = new TradeMembershipEntity();
		result.setTrade(tradeEntity);
		result.setUser(user);
		return result;
	}

}