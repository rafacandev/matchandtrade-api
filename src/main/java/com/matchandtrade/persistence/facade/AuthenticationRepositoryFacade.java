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

	// TODO: Optional
	public AuthenticationEntity find(Integer authenticationId) {
		return authenticationRepository.findById(authenticationId).get();
	}
	
	/**
	 * Find {@code AuthenticationEntity} by {@code antiForgeryState}.
	 * It returns {@code null} if no results found or if {@code antiForgeryState} is {@code null}
	 * @param antiForgeryState
	 * @return
	 */
	public AuthenticationEntity findByAtiForgeryState(String antiForgeryState) {
		if (antiForgeryState == null) {
			return null;
		}
		return authenticationRepository.findByAntiForgeryState(antiForgeryState);
	}

	/**
	 * Find {@code AuthenticationEntity} by {@code token}.
	 * It returns {@code null} if no results found or if {@code token} is {@code null}.
	 * 
	 * @param token
	 * @return
	 */
	public AuthenticationEntity findByToken(String token) {
		if (token == null) {
			return null;
		}
		return authenticationRepository.findByToken(token);
	}

	public void save(AuthenticationEntity entity) {
		authenticationRepository.save(entity);
	}

}
