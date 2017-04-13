package com.matchandtrade.persistence.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.criteria.UserCriteriaBuilder;
import com.matchandtrade.persistence.entity.UserEntity;


@Component
public class UserDao extends Dao<UserEntity> {

	@Autowired
	private UserCriteriaBuilder criteriaBuilder;
	@Autowired
	private SearchDao<UserEntity> searchableDao;
	
	public SearchResult<UserEntity> search(SearchCriteria searchCriteria) {
		return searchableDao.search(searchCriteria, criteriaBuilder);
	}

}
