package com.matchandtrade.persistence.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.entity.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer>{
	
	UserEntity findByEmail(String email);

	@Query("SELECT u "
			+ " FROM TradeMembershipEntity tm"
			+ " INNER JOIN tm.user AS u"
			+ " INNER JOIN tm.trade AS t"
			+ " INNER JOIN tm.items AS i"
			+ " WHERE"
			+ " i.itemId = :itemId")
	UserEntity findByItemId(@Param("itemId") Integer itemId);

}
