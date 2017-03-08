package com.matchandtrade.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.common.Pagination;
import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.dao.AuthenticationDao;
import com.matchandtrade.persistence.entity.AuthenticationEntity;

@Component
public class AuthenticationModel {

	@Autowired
	private AuthenticationDao authenticationDao;

	@Transactional
	public AuthenticationEntity get(Integer authenticationId) {
		return authenticationDao.get(AuthenticationEntity.class, authenticationId);
	}

	@Transactional
	public AuthenticationEntity getByToken(String token) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination());
		searchCriteria.addCriterion(AuthenticationEntity.Field.token, token);
		SearchResult<AuthenticationEntity> searchResult = search(searchCriteria);
		if (!searchResult.getResultList().isEmpty()) {
			return searchResult.getResultList().get(0);
		} else {
			return null;
		}
	}
	
	@Transactional
	public void save(AuthenticationEntity entity) {
		authenticationDao.save(entity);
	}

	@Transactional
	public SearchResult<AuthenticationEntity> search(SearchCriteria searchCriteria) {
		return authenticationDao.search(searchCriteria);
	}

}
