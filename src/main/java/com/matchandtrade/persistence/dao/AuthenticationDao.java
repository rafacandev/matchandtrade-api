package com.matchandtrade.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.criteria.AuthenticationCriteriaBuilder;
import com.matchandtrade.persistence.entity.AuthenticationEntity;


@Component
public class AuthenticationDao extends Dao<AuthenticationEntity> {

	@Autowired
	private SearchDao<AuthenticationEntity> searchableDao;
	@Autowired
	private AuthenticationCriteriaBuilder criteriaBuilder;

	public SearchResult<AuthenticationEntity> search(SearchCriteria searchCriteria) {
		return searchableDao.search(searchCriteria, criteriaBuilder);
	}

}
