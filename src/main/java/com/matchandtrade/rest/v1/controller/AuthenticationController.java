package com.matchandtrade.rest.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.RestException;
import com.matchandtrade.rest.v1.json.AuthenticationJson;
import com.matchandtrade.rest.v1.link.AuthenticationLinkAssember;
import com.matchandtrade.rest.v1.transformer.AuthenticationTransformer;

@RestController
@RequestMapping(path = "/rest/v1/authentications/")
public class AuthenticationController {

	@Autowired
	AuthenticationProvider authenticationProvider;
	
	@RequestMapping(path = "", method = RequestMethod.GET)
	public AuthenticationJson get() {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Get authentication object
		AuthenticationEntity authenticationEntity = authenticationProvider.getAuthentication();
		// Throw RestException HttpStatus.UNAUTHORIZED if there is no UserAuthentication meaning that the user is not authenticated
		if (authenticationEntity == null) {
			throw new RestException(HttpStatus.UNAUTHORIZED);
		}
		// Transform the response
		AuthenticationJson response = AuthenticationTransformer.transform(authenticationEntity);
		// Assemble links
		AuthenticationLinkAssember.assemble(response);
		return response;
	}

}
