package com.matchandtrade.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer>{
	
	UserEntity findByEmail(String email);

}
