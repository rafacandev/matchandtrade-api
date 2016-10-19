package com.matchandtrade.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.common.SearchCriteria;
import com.matchandtrade.common.SearchResult;
import com.matchandtrade.persistence.dao.TradeListDao;
import com.matchandtrade.persistence.dao.UserDao;
import com.matchandtrade.persistence.entity.TradeListEntity;
import com.matchandtrade.persistence.entity.UserEntity;

@Component
public class TradeListModel {
	
	@Autowired
	TradeListDao tradeListDao;
	@Autowired
	UserDao userDao;

	public TradeListEntity get(Integer tradeListId) {
		return tradeListDao.get(tradeListId);
	}

	@Transactional
	public void save(Integer userId, TradeListEntity entity) {
    	UserEntity userEntity = userDao.get(userId);
    	userEntity.getTradeLists().add(entity);
    	userDao.save(userEntity);
	}
	
	public void save(TradeListEntity tradeListEntity) {
		tradeListDao.save(tradeListEntity);
	}
	
	public SearchResult<TradeListEntity> search(SearchCriteria sc) {
    	SearchResult<TradeListEntity> result = tradeListDao.search(sc);
    	return result;
	}
	
}
