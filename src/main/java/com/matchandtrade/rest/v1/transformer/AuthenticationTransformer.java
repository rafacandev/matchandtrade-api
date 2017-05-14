package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.rest.v1.json.AuthenticationJson;

public class AuthenticationTransformer {
	
	// Utility classes should not have public constructors 
	private AuthenticationTransformer() {}
	
	public static AuthenticationJson transform(AuthenticationEntity entity) {
		AuthenticationJson result = new AuthenticationJson();
		result.setUserId(entity.getUser().getUserId());
		return result;
	}
	
}
