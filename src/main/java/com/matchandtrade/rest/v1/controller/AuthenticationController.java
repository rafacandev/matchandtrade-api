package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.v1.json.AuthenticationJson;
import com.matchandtrade.rest.v1.transformer.AuthenticationTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/matchandtrade-api/v1/authentications")
public class AuthenticationController implements Controller {

	@Autowired
	AuthenticationProvider authenticationProvider;
	private AuthenticationTransformer authenticationTransformer = new AuthenticationTransformer();
	
	@RequestMapping(path = "/", method = RequestMethod.GET)
	public AuthenticationJson get() {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request - Nothing to validate
		// Delegate to service layer - Nothing to delegate
		// Transform the response
		AuthenticationJson response = authenticationTransformer.transform(authenticationProvider.getAuthentication());
		// TODO: Assemble links
		return response;
	}

}
