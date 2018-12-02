package com.matchandtrade.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.matchandtrade.persistence.entity.AuthenticationEntity;

public interface AuthenticationRepository extends CrudRepository<AuthenticationEntity, Integer>{
	AuthenticationEntity findByAntiForgeryState(String antiForgeryState);
	
	AuthenticationEntity findByToken(String token);
}
