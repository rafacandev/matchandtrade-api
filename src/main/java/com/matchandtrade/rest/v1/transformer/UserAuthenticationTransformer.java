package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.authentication.UserAuthentication;
import com.matchandtrade.rest.v1.json.UserAuthenticationJson;

public class UserAuthenticationTransformer {
	
	public static UserAuthenticationJson transform(UserAuthentication userAuthentication) {
		if (userAuthentication == null) {
			return null;
		}
		UserAuthenticationJson result = new UserAuthenticationJson();
		result.setAuthenticated(userAuthentication.isAuthenticated());
		result.setEmail(userAuthentication.getEmail());
		result.setName(userAuthentication.getName());
		result.setNewUser(userAuthentication.isNewUser());
		result.setUserId(userAuthentication.getUserId());
		return result;
	}
	
}
