package com.matchandtrade.persistence.facade;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.PersistenceUtil;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.entity.WantItemEntity;
import com.matchandtrade.persistence.repository.WantItemRepository;

@Repository
public class WantItemRepositoryFacade {
	
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private WantItemRepository wantItemRepository;

    /**
     * Count how many 'WantItem' are there for a given {@code itemId}
     * @param itemId
     * @param priority
     * @return
     */
	public long countWantItemPriorityInItem(Integer itemId, Integer priority) {
		Query query = entityManager.createQuery("SELECT COUNT(*) FROM ItemEntity item INNER JOIN item.wantItems AS wantedItem WHERE item.itemId = :itemId AND wantedItem.priority = :priority");
		query.setParameter("itemId", itemId);
		query.setParameter("priority", priority);
		return (Long) query.getSingleResult();
	}

	/**
	 * Count how many 'WantItem' are there for a given itemId
	 * @param itemId
	 * @param desiredItemId
	 * @return
	 */
	public long countWantItemInItem(Integer itemId, Integer desiredItemId) {
		Query query = entityManager.createQuery("SELECT COUNT(*) FROM ItemEntity item INNER JOIN item.wantItems AS wantedItem WHERE item.itemId = :itemId AND wantedItem.item.itemId = :desiredItemId");
		query.setParameter("itemId", itemId);
		query.setParameter("desiredItemId", desiredItemId);
		return (Long) query.getSingleResult();
	}

	public SearchResult<WantItemEntity> findByTradeMembershipAndItemId(Integer tradeMembershipId, Integer itemId, Pagination pagination) {
		Pageable pageable = PersistenceUtil.buildPageable(pagination.getNumber(), pagination.getSize());
		Page<WantItemEntity> page = wantItemRepository.findByTradeMembershipIdAndItemId(tradeMembershipId, itemId, pageable);
		return PersistenceUtil.buildSearchResult(pageable, page);
	}

	public WantItemEntity findByTradeMembershipAndItemIdAndWantItemId(Integer tradeMembershipId, Integer itemId, Integer wantItemId) {
		return wantItemRepository.findByTradeMembershipAndItemIdAndWantItemId(tradeMembershipId, itemId, wantItemId);
	}
}
