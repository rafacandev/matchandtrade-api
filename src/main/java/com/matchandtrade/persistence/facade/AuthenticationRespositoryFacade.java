package com.matchandtrade.persistence.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.common.SearchCriteria;
import com.matchandtrade.persistence.common.SearchResult;
import com.matchandtrade.persistence.criteria.AuthenticationQueryBuilderJavax;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.repository.AuthenticationRepository;

@Repository
public class AuthenticationRespositoryFacade {

	@Autowired
	private AuthenticationRepository authenticationRepository;
	@Autowired
	private QueryableRepositoryJavax<AuthenticationEntity> queryableRepository;
	@Autowired
	private AuthenticationQueryBuilderJavax authenticationQueryBuilder;

	public void delete(AuthenticationEntity entity) {
		authenticationRepository.save(entity);
	}

	public AuthenticationEntity get(Integer id) {
		return authenticationRepository.findOne(id);
	}
	
	public AuthenticationEntity getByAtiForgeryState(String antiForgeryState) {
		if (antiForgeryState == null) {
			return null;
		} else {
			return authenticationRepository.findByAntiForgeryState(antiForgeryState);
		}
	}

	public AuthenticationEntity getByToken(String token) {
		if (token == null) {
			return null;
		} else {
			return authenticationRepository.findByToken(token);
		}
	}

	public void save(AuthenticationEntity entity) {
		authenticationRepository.save(entity);
	}

	public SearchResult<AuthenticationEntity> search(SearchCriteria searchCriteria) {
		return queryableRepository.query(searchCriteria, authenticationQueryBuilder);
	}

}
