package com.matchandtrade.persistence.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.repository.AuthenticationRepository;

@Repository
public class AuthenticationRepositoryFacade {
	@Autowired
	private AuthenticationRepository authenticationRepository;

	public void delete(AuthenticationEntity entity) {
		authenticationRepository.save(entity);
	}

	public AuthenticationEntity findByAtiForgeryState(String antiForgeryState) {
		return authenticationRepository.findByAntiForgeryState(antiForgeryState);
	}

	public AuthenticationEntity findByToken(String token) {
		return authenticationRepository.findByToken(token);
	}

	public void save(AuthenticationEntity entity) {
		authenticationRepository.save(entity);
	}
}
