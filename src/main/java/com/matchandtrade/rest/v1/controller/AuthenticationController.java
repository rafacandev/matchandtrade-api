package com.matchandtrade.rest.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authorization.AuthorizationException;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.v1.json.AuthenticationJson;
import com.matchandtrade.rest.v1.transformer.AuthenticationTransformer;

@RestController
@RequestMapping(path = "/rest/v1/authentications/")
public class AuthenticationController {

	@Autowired
	AuthenticationProvider authenticationProvider;
	
	@RequestMapping(path = "", method = RequestMethod.GET)
	public AuthenticationJson get() {
		// Get user authentication
		AuthenticationEntity authenticationEntity = authenticationProvider.getAuthentication();
		// Throw AuthorizationException if there is no UserAuthentication. User is not authenticated.
		if (authenticationEntity == null) {
			throw new AuthorizationException(AuthorizationException.Type.UNAUTHORIZED);
		}
		// Transform the response
		AuthenticationJson result = AuthenticationTransformer.transform(authenticationEntity);
		return result;
	}
}
