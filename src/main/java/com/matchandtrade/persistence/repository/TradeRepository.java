package com.matchandtrade.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.entity.TradeEntity;

@Repository
public interface TradeRepository extends CrudRepository<TradeEntity, Integer>{

	@Query("SELECT trade FROM TradeMembershipEntity AS tradeMembership"
			+ " INNER JOIN tradeMembership.trade AS trade"
			+ " INNER JOIN tradeMembership.items AS item"
			+ " WHERE"
			+ " item.itemId IN (:itemIds)"
			+ " GROUP BY trade")
	Page<TradeEntity> findInItemIdsGroupByTrade(@Param("itemIds") Integer[] itemIds, Pageable pageable);
	
}
