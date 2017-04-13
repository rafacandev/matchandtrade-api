package com.matchandtrade.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.criteria.TradeCriteriaBuilder;
import com.matchandtrade.persistence.entity.TradeEntity;


@Component
public class TradeDao extends Dao<TradeEntity> {

	@Autowired
	private TradeCriteriaBuilder criteriaBuilder;
	@Autowired
	private SearchDao<TradeEntity> searchableDao;

	public SearchResult<TradeEntity> search(SearchCriteria searchCriteria) {
		return searchableDao.search(searchCriteria, criteriaBuilder);
	}
	
}
