package com.matchandtrade.repository;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.WantItemQueryBuilder;
import com.matchandtrade.persistence.entity.WantItemEntity;

@Repository
public class WantItemRepository {
	
    @Autowired
    private SessionFactory sessionFactory;
	@Autowired
	private QueryableRepository<WantItemEntity> queryableRepository;
	@Autowired
	private WantItemQueryBuilder queryBuilder;

	@Transactional
	public SearchResult<WantItemEntity> query(SearchCriteria searchCriteria) {
		return queryableRepository.query(searchCriteria, queryBuilder);
	}
	
	@Transactional
	public int countItemWantItemPriority(Integer itemId, Integer priority) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT COUNT(*) FROM ItemEntity item INNER JOIN item.wantItems AS wantedItem WHERE item.itemId = :itemId AND wantedItem.priority = :priority");
		query.setParameter("itemId", itemId);
		query.setParameter("priority", priority);
		Long count = (Long) query.uniqueResult();
		return count.intValue();
	}

	@Transactional
	public int countItemWantItem(Integer itemId, Integer desiredItemId) {
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("SELECT COUNT(*) FROM ItemEntity item INNER JOIN item.wantItems AS wantedItem WHERE item.itemId = :itemId AND wantedItem.item.itemId = :desiredItemId");
		query.setParameter("itemId", itemId);
		query.setParameter("desiredItemId", desiredItemId);
		Long count = (Long) query.uniqueResult();
		return count.intValue();
	}
	
}
