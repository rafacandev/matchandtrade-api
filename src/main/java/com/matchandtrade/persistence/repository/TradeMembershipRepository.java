package com.matchandtrade.persistence.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.entity.TradeMembershipEntity;

@Repository
public interface TradeMembershipRepository extends CrudRepository<TradeMembershipEntity, Integer>{

	List<TradeMembershipEntity> findByTrade_TradeId(Integer tradeId);
	
}
