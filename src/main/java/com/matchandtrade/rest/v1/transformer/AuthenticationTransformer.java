package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.rest.v1.json.AuthenticationJson;

public class AuthenticationTransformer {
	
	public static AuthenticationJson transform(AuthenticationEntity entity) {
		if (entity == null) {
			return null;
		}
		AuthenticationJson result = new AuthenticationJson();
		result.setAuthenticationId(entity.getAuthenticationId());
		result.setToken(entity.getToken());
		result.setUserId(entity.getUserId());
		return result;
	}
	
}
