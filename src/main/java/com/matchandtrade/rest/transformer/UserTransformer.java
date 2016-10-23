package com.matchandtrade.rest.transformer;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.json.UserJson;

public class UserTransformer {
	
	public static UserEntity transform(UserJson json) {
		return transform(json, null);
	}
	
	public static UserEntity transform(UserJson json, UserEntity entity) {
		if (json == null) {
			return null;
		}
		
		UserEntity result;
		if (entity != null) {
			result = entity;
		} else {
			result = new UserEntity();
		}
		
		result.setEmail(json.getEmail());
		result.setName(json.getName());
		result.setUserId(json.getUserId());
		
		return result;
	}
	
	public static UserJson transform(UserEntity entity) {
		if (entity == null) {
			return null;
		}
		UserJson result = new UserJson();
		result.setEmail(entity.getEmail());
		result.setName(entity.getName());
		result.setUserId(entity.getUserId());
		
		return result;
	}
	
}
