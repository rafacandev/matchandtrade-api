package com.matchandtrade.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.matchandtrade.persistence.entity.WantItemEntity;

public interface WantItemRepository extends CrudRepository<WantItemEntity, Integer>{
	
	@Query("SELECT wantItem"
			+ " FROM TradeMembershipEntity tradeMembership"
			+ " INNER JOIN tradeMembership.items item"
			+ " INNER JOIN item.wantItems wantItem"
			+ " WHERE"
			+ " tradeMembership.tradeMembershipId = :tradeMembershipId"
			+ " AND item.itemId = :itemId")
	Page<WantItemEntity> findByTradeMembershipIdAndItemId(@Param("tradeMembershipId") Integer tradeMembershipId, @Param("itemId")Integer itemId, Pageable pageable);

	@Query("SELECT wantItem"
			+ " FROM TradeMembershipEntity tradeMembership"
			+ " INNER JOIN tradeMembership.items item"
			+ " INNER JOIN item.wantItems wantItem"
			+ " WHERE"
			+ " tradeMembership.tradeMembershipId = :tradeMembershipId"
			+ " AND item.itemId = :itemId"
			+ " AND wantItem.wantItemId = :wantItemId")
	WantItemEntity findByTradeMembershipAndItemIdAndWantItemId(@Param("tradeMembershipId") Integer tradeMembershipId, @Param("itemId")Integer itemId, @Param("wantItemId")Integer wantItemId);

}
