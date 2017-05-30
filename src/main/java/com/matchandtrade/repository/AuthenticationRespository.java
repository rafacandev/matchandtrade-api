package com.matchandtrade.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.AuthenticationQueryBuilder;
import com.matchandtrade.persistence.entity.AuthenticationEntity;

@Repository
public class AuthenticationRespository {

	@Autowired
	private BasicRepository<AuthenticationEntity> authenticationDao;
	@Autowired
	private QueryableRepository<AuthenticationEntity> searchableRepository;
	@Autowired
	private AuthenticationQueryBuilder criteriaBuilder;

	@Transactional
	public AuthenticationEntity getByAtiForgeryState(String antiForgeryState) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination());
		searchCriteria.addCriterion(AuthenticationQueryBuilder.Criterion.antiForgeryState, antiForgeryState);
		SearchResult<AuthenticationEntity> searchResult = search(searchCriteria);
		if (!searchResult.getResultList().isEmpty()) {
			return searchResult.getResultList().get(0);
		} else {
			return null;
		}
	}

	@Transactional
	public AuthenticationEntity getByToken(String token) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination());
		searchCriteria.addCriterion(AuthenticationQueryBuilder.Criterion.token, token);
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
		return searchableRepository.query(searchCriteria, criteriaBuilder);
	}

	@Transactional
	public void delete(AuthenticationEntity entity) {
		authenticationDao.delete(entity);
	}

}
