package com.matchandtrade.test.random;

import com.matchandtrade.rest.v1.json.UserJson;

public class UserRandom {
	
	public static UserJson next() {
		StringRandom random = new StringRandom();
		UserJson result = new UserJson();
		result.setName(random.nextName());
		result.setEmail(random.nextEmail());
		return result;
	}
	
	public static UserJson next(Integer userId) {
		UserJson result = next();
		result.setUserId(userId);
		return result;
	}

}