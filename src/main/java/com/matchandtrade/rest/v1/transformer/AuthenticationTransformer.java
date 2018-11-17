package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.rest.v1.json.AuthenticationJson;

public class AuthenticationTransformer extends Transformer<AuthenticationEntity, AuthenticationJson> {
	
	@Override
	public AuthenticationJson transform(AuthenticationEntity entity) {
		AuthenticationJson result = new AuthenticationJson();
		result.setUserId(entity.getUser().getUserId());
		return result;
	}

	@Override
	public AuthenticationEntity transform(AuthenticationJson json) {
		// So far nobody needs this method. Fell free to implement it when needed.
		throw new UnsupportedOperationException();
	}

}
