package com.matchandtrade.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.common.Pagination;
import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.UserCriteriaBuilder;
import com.matchandtrade.persistence.entity.UserEntity;

@Repository
public class UserRepository {
	
	@Autowired
	private BasicRepository<UserEntity> basicRepository;
	@Autowired
	private SearchableRepository<UserEntity> searchableRepository;
	@Autowired
	private UserCriteriaBuilder criteriaBuilder;

	@Transactional
	public UserEntity get(Integer userId) {
		return basicRepository.get(UserEntity.class, userId);
	}

	@Transactional
	public UserEntity get(String email) {
		SearchCriteria searchCriteria = new SearchCriteria(new Pagination());
		searchCriteria.addCriterion(UserCriteriaBuilder.Criterion.email, email);
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
		return searchableRepository.search(searchCriteria, criteriaBuilder);
	}

}
