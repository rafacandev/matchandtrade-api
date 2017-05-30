package com.matchandtrade.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.UserQueryBuilder;
import com.matchandtrade.persistence.entity.UserEntity;

@Repository
public class UserRepository {
	
	@Autowired
	private BasicRepository<UserEntity> basicRepository;
	@Autowired
	private QueryableRepository<UserEntity> queryableRepository;
	@Autowired
	private UserQueryBuilder criteriaBuilder;

	@Transactional
	public UserEntity get(Integer userId) {
		return basicRepository.get(UserEntity.class, userId);
	}

	@Transactional
	public UserEntity get(String email) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination());
		searchCriteria.addCriterion(UserQueryBuilder.Criterion.email, email);
		SearchResult<UserEntity> searchResult = search(searchCriteria);
		if (!searchResult.getResultList().isEmpty()) {
			return searchResult.getResultList().get(0);
		} else {
			return null;
		}
	}

	@Transactional
	public void save(UserEntity entity) {
		basicRepository.save(entity);
	}

	@Transactional
	public SearchResult<UserEntity> search(SearchCriteria searchCriteria) {
		return queryableRepository.query(searchCriteria, criteriaBuilder);
	}

}
