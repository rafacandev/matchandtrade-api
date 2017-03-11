package com.matchandtrade.test.random;

import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.json.UserJson;

public class UserRandom {
	
	public static UserEntity nextEntity() {
		UserEntity result = new UserEntity();
		result.setName(StringRandom.nextName());
		result.setEmail(StringRandom.nextEmail());
		return result;
	}

	public static UserJson nextJson() {
		UserJson result = new UserJson();
		result.setName(StringRandom.nextName());
		result.setEmail(StringRandom.nextEmail());
		return result;
	}
	
	public static UserJson nextJson(Integer userId) {
		UserJson result = nextJson();
		result.setUserId(userId);
		return result;
	}

}