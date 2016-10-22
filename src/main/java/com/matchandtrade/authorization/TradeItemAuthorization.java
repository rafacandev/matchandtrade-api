package com.matchandtrade.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matchandtrade.authentication.UserAuthentication;
import com.matchandtrade.model.TradeItemModel;
import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.TradeItemEntity;
import com.matchandtrade.persistence.entity.TradeListEntity;
import com.matchandtrade.persistence.entity.UserEntity;


@Component
public class TradeItemAuthorization extends Authorization {
	
	@Autowired
	private TradeItemModel model;
	
	@Autowired
	private UserModel userModel;
	
	@Transactional
	public void verifyIfUserIsAuthorizedOverTheResource(UserAuthentication userAuthentication, Integer tradeItemId) {
		TradeItemEntity entity = model.get(tradeItemId);
		UserEntity userEntity = userModel.get(userAuthentication.getUserId());
		// TODO: Improve performance here
		boolean contains = false;
		for (TradeListEntity tl: userEntity.getTradeLists()) {
			if (tl.getTradeItems().contains(entity)) {
				contains = true;
			}
		}
		if (!contains) {
			new AuthorizationException(AuthorizationException.Type.USER_NOT_AUTHORIZED);
		}
	}
}
