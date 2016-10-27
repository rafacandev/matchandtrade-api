package com.matchandtrade.rest.transformer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.json.UserJson;

@Component
public class UserTransformer {
	
	@Autowired
	private UserModel userModel;

	public UserEntity transform(UserJson json, boolean loadEntity) {
		UserEntity result;
		if (loadEntity) {
			result = userModel.get(json.getUserId());
		} else {
			result = new UserEntity();
		}
		result.setEmail(json.getEmail());
		result.setName(json.getName());
		result.setUserId(json.getUserId());
		return result;
	}
	
	public UserEntity transform(UserJson json) {
		return transform(json, false);
	}
	
	public static UserJson transform(UserEntity entity) {
		UserJson result = new UserJson();
		result.setEmail(entity.getEmail());
		result.setName(entity.getName());
		result.setUserId(entity.getUserId());
		return result;
	}
	
}
