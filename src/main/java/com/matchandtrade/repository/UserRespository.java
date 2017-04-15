package com.matchandtrade.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.dao.UserDao;
import com.matchandtrade.persistence.entity.UserEntity;

@Repository
public class UserRespository {

	@Autowired
	UserDao userDao;

	@Transactional
	public UserEntity get(Integer userId) {
		return userDao.get(UserEntity.class, userId);
	}

	@Transactional
	public UserEntity get(String email) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination());
		searchCriteria.addCriterion(UserEntity.Field.email, email);
		SearchResult<UserEntity> searchResult = search(searchCriteria);
		if (!searchResult.getResultList().isEmpty()) {
			return searchResult.getResultList().get(0);
		} else {
			return null;
		}
	}

	@Transactional
	public void save(UserEntity entity) {
		userDao.save(entity);
	}

	@Transactional
	public SearchResult<UserEntity> search(SearchCriteria searchCriteria) {
		return userDao.search(searchCriteria);
	}

}
