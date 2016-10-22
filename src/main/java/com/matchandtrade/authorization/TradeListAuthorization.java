package com.matchandtrade.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.model.TradeListModel;
import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.TradeListEntity;
import com.matchandtrade.persistence.entity.UserEntity;


@Component
public class TradeListAuthorization extends Authorization {
	
	@Autowired
	private TradeListModel model;
	
	@Autowired
	private UserModel userModel;
	
	@Transactional
	public void authorizeResource(Integer userId, Integer tradeListId) {
		TradeListEntity tradeListEntity = model.get(tradeListId);
		UserEntity userEntity = userModel.get(userId);
		// TODO: Improve performance here
		if (!userEntity.getTradeLists().contains(tradeListEntity)) {
			throw new AuthorizationException(AuthorizationException.Type.USER_NOT_AUTHORIZED);
		}
	}
}
