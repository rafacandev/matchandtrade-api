package com.matchandtrade.persistence.repository;

import com.matchandtrade.persistence.entity.TradeEntity;
import org.springframework.data.repository.CrudRepository;

public interface TradeRepository extends CrudRepository<TradeEntity, Integer>{
}
