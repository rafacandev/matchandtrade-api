package com.matchandtrade.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.dao.TradeDao;
import com.matchandtrade.persistence.entity.TradeEntity;
import com.matchandtrade.persistence.entity.UserEntity;

@Component
public class TradeModel {

	@Autowired
	private TradeDao tradeDao;

	@Transactional
	public TradeEntity get(Integer tradeId) {
		return tradeDao.get(TradeEntity.class, tradeId);
	}

	@Transactional
	public void save(TradeEntity entity) {
		tradeDao.save(entity);
	}
	
	@Transactional
	public SearchResult<TradeEntity> search(SearchCriteria searchCriteria) {
		return tradeDao.search(searchCriteria);
	}

}
