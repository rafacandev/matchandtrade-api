package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.json.UserJson;

public class UserTransformer extends Transformer<UserEntity, UserJson> {

	@Override
	public UserEntity transform(UserJson json) {
		UserEntity result;
		result = new UserEntity();
		result.setEmail(json.getEmail());
		result.setName(json.getName());
		result.setUserId(json.getUserId());
		return result;
	}

	@Override
	public UserJson transform(UserEntity entity) {
		UserJson result = new UserJson();
		result.setEmail(entity.getEmail());
		result.setName(entity.getName());
		result.setUserId(entity.getUserId());
		return result;
	}

}
