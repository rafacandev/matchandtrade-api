package com.matchandtrade.persistence.repository;

import com.matchandtrade.persistence.entity.TradeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends CrudRepository<TradeEntity, Integer>{

}
