package com.matchandtrade.persistence.facade;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WantItemRepositoryFacade {
	
    @Autowired
    private EntityManager entityManager;

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
	
}
