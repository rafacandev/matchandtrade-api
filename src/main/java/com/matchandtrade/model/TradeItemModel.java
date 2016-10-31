package com.matchandtrade.model;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.dao.TradeItemDao;
import com.matchandtrade.persistence.dao.TradeListDao;
import com.matchandtrade.persistence.entity.TradeItemEntity;
import com.matchandtrade.persistence.entity.TradeListEntity;

@Component
public class TradeItemModel {
	
	@Autowired
	private TradeListDao tradeListDao;
	@Autowired
	private TradeItemDao tradeItemDao;
	
	public void save(TradeItemEntity entity) {
    	entity.setUpdatedDateTime(new Date());
		tradeItemDao.save(entity);
	}

	@Transactional
	public TradeItemEntity save(Integer tradeListId, TradeItemEntity entity) {
		TradeListEntity tradeListEntity = tradeListDao.get(tradeListId);
		entity.setUpdatedDateTime(new Date());
		tradeListEntity.getTradeItems().add(entity);
		tradeListDao.save(tradeListEntity);
		return entity;
	}

	public TradeItemEntity get(Integer tradeItemId) {
		return tradeItemDao.get(TradeItemEntity.class, tradeItemId);
	}

}
