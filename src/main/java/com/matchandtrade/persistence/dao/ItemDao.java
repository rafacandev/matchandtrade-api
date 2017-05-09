package com.matchandtrade.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.criteria.ItemQueryBuilder;
import com.matchandtrade.persistence.entity.ItemEntity;


@Component
public class ItemDao extends Dao<ItemEntity> {
	
	@Autowired
	private QueryDao<ItemEntity> queryDao;
	@Autowired
	private ItemQueryBuilder queryBuilder;
	
	public SearchResult<ItemEntity> query(SearchCriteria searchCriteria) {
		return queryDao.query(searchCriteria, queryBuilder);
	}

}
