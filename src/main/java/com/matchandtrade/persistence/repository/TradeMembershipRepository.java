package com.matchandtrade.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.entity.TradeMembershipEntity;

@Repository
public interface TradeMembershipRepository extends CrudRepository<TradeMembershipEntity, Integer>{

	List<TradeMembershipEntity> findByTrade_TradeId(Integer tradeId);
	
	Page<TradeMembershipEntity> findByTrade_TradeIdAndUser_UserId(Integer tradeId, Integer userId, Pageable pageable);

}
