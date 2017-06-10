package com.matchandtrade.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.matchandtrade.persistence.entity.TradeEntity;

public interface TradeRepository extends CrudRepository<TradeEntity, Integer>{
	
	Page<TradeEntity> findByNameIgnoreCase(String name, Pageable pageable);

}
