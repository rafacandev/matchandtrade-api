package com.matchandtrade.persistence.facade;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.WantItemQueryBuilder;
import com.matchandtrade.persistence.entity.WantItemEntity;

@Repository
public class WantItemRepositoryFacade {
	
    @Autowired
    private EntityManager entityManager;

	public int countItemWantItemPriority(Integer itemId, Integer priority) {
		Query query = entityManager.createQuery("SELECT COUNT(*) FROM ItemEntity item INNER JOIN item.wantItems AS wantedItem WHERE item.itemId = :itemId AND wantedItem.priority = :priority");
		query.setParameter("itemId", itemId);
		query.setParameter("priority", priority);
		Long count = (Long) query.getSingleResult();
		return count.intValue();
	}

	public int countItemWantItem(Integer itemId, Integer desiredItemId) {
		Query query = entityManager.createQuery("SELECT COUNT(*) FROM ItemEntity item INNER JOIN item.wantItems AS wantedItem WHERE item.itemId = :itemId AND wantedItem.item.itemId = :desiredItemId");
		query.setParameter("itemId", itemId);
		query.setParameter("desiredItemId", desiredItemId);
		Long count = (Long) query.getSingleResult();
		return count.intValue();
	}
	
}
