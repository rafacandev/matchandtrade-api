package com.matchandtrade.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.criteria.WantItemQueryBuilder;
import com.matchandtrade.persistence.entity.WantItemEntity;

@Repository
public class WantItemRepository {
	
	@Autowired
	private BasicRepository<WantItemEntity> basicRepository;
	@Autowired
	private QueryableRepository<WantItemEntity> queryableRepository;
	@Autowired
	private WantItemQueryBuilder queryBuilder;

	@Transactional
	public SearchResult<WantItemEntity> query(SearchCriteria searchCriteria) {
		return queryableRepository.query(searchCriteria, queryBuilder);
	}
	
	@Transactional
	public void save(WantItemEntity entity) {
		basicRepository.save(entity);
	}
	
}
