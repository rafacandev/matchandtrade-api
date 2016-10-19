package com.matchandtrade.model;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.persistence.dao.UserDao;
import com.matchandtrade.persistence.entity.TradeListEntity;
import com.matchandtrade.persistence.entity.UserEntity;

@Component
public class UserModel {
	
	@Autowired
	UserDao userDao;

	@Transactional
	public UserEntity get(Integer userId) {
    	UserEntity userEntity = userDao.get(userId);
    	return userEntity;
	}
	
	@Transactional
	public UserEntity save(UserEntity entity) {
		userDao.save(entity);
		return entity;
	}

	@Transactional
	public void saveTradeList(Integer userId, TradeListEntity tradeListEntity) {
			UserEntity userEntity = userDao.get(userId);
			userEntity.getTradeLists().add(tradeListEntity);
			userDao.save(userEntity);
	}

}
