package com.matchandtrade.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.ItemQueryBuilder;
import com.matchandtrade.persistence.entity.ItemEntity;

@Repository
public class ItemRepository {
	
	@Autowired
	private BasicRepository<ItemEntity> basicRepository;
	@Autowired
	private QueryableRepository<ItemEntity> queryableRepository;
	@Autowired
	private ItemQueryBuilder queryBuilder;

	@Transactional
	public ItemEntity get(Integer itemId) {
		return basicRepository.get(ItemEntity.class, itemId);
	}

	@Transactional
	public SearchResult<ItemEntity> query(SearchCriteria searchCriteria) {
		return queryableRepository.query(searchCriteria, queryBuilder);
	}
	
	@Transactional
	public void save(ItemEntity entity) {
		basicRepository.save(entity);
	}
	
}
