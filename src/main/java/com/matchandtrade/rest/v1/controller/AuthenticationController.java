package com.matchandtrade.rest.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.v1.json.AuthenticationJson;
import com.matchandtrade.rest.v1.link.AuthenticationLinkAssember;
import com.matchandtrade.rest.v1.transformer.AuthenticationTransformer;

@RestController
@RequestMapping(path = "/rest/v1/authentications/")
public class AuthenticationController implements Controller {

	@Autowired
	AuthenticationProvider authenticationProvider;
	
	@RequestMapping(path = "", method = RequestMethod.GET)
	public AuthenticationJson get() {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request - Nothing to validate
		// Delegate to service layer - Nothing to delegate
		// Transform the response
		AuthenticationJson response = AuthenticationTransformer.transform(authenticationProvider.getAuthentication());
		// Assemble links
		AuthenticationLinkAssember.assemble(response);
		return response;
	}

}
