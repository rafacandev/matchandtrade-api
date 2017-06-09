package com.matchandtrade.persistence.repository;

import org.springframework.data.repository.CrudRepository;

import com.matchandtrade.persistence.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Integer>{
	
	UserEntity findByEmail(String email);

}
