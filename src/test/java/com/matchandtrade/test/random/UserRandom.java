package com.matchandtrade.test.random;

import com.matchandtrade.rest.v1.json.UserJson;

public class UserRandom {
	
	public static UserJson next() {
		UserJson result = new UserJson();
		result.setName(StringRandom.nextName());
		result.setEmail(StringRandom.nextEmail());
		return result;
	}
	
	public static UserJson next(Integer userId) {
		UserJson result = next();
		result.setUserId(userId);
		return result;
	}

}