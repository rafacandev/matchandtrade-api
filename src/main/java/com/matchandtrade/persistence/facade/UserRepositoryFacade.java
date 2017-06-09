package com.matchandtrade.persistence.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.UserQueryBuilderJavax;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.repository.UserRepository;

@Repository
public class UserRepositoryFacade {
	
	@Autowired
	private QueryableRepositoryJavax<UserEntity> queryableRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserQueryBuilderJavax userQueryBuilder;

	public UserEntity get(Integer userId) {
		return userRepository.findOne(userId);
	}

	public UserEntity getByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public void save(UserEntity entity) {
		userRepository.save(entity);
	}

	public SearchResult<UserEntity> search(SearchCriteria searchCriteria) {
		return queryableRepository.query(searchCriteria, userQueryBuilder);
	}

}
