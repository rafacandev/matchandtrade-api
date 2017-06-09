package com.matchandtrade.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.matchandtrade.persistence.entity.TradeMembershipEntity;

public interface TradeMembershipRepository extends CrudRepository<TradeMembershipEntity, Integer>{

	public List<TradeMembershipEntity> findByTrade_TradeId(Integer tradeId);

}
