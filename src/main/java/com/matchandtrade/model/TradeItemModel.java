package com.matchandtrade.model;

import java.util.Date;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
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

	public SearchResult<TradeItemEntity> search(SearchCriteria searchCriteria) {
		// By default results are sorted by updatedDateTime
		if (searchCriteria.getOrderBy().isEmpty()) {
			//TODO Refactor SearchCriteria.Order
			searchCriteria.addOrderBy(Order.desc("ti."+TradeItemEntity.Field.updatedDateTime.toString()));
		}
    	SearchResult<TradeItemEntity> result = tradeItemDao.search(searchCriteria);
    	return result;
	}
	
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
		return tradeItemDao.get(tradeItemId);
	}

}
