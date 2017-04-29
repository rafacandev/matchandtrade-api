package com.matchandtrade.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.criteria.TradeMembershipCriteriaBuilder;
import com.matchandtrade.persistence.entity.TradeMembershipEntity;


@Component
public class TradeMembershipDao extends Dao<TradeMembershipEntity> {
	
	@Autowired
	private TradeMembershipCriteriaBuilder criteriaBuilder;
	@Autowired
	private SearchDao<TradeMembershipEntity> searchableDao;

	public SearchResult<TradeMembershipEntity> search(SearchCriteria searchCriteria) {
		return searchableDao.search(searchCriteria, criteriaBuilder);
	}

}
