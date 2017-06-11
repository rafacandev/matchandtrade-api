package com.matchandtrade.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.matchandtrade.persistence.entity.TradeEntity;

@Repository
public interface TradeRepository extends CrudRepository<TradeEntity, Integer>{
	
	Page<TradeEntity> findByNameIgnoreCase(String name, Pageable pageable);

}
