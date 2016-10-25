package com.matchandtrade.rest.v1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authentication.UserAuthentication;
import com.matchandtrade.authorization.AuthorizationException;
import com.matchandtrade.rest.Controller;
import com.matchandtrade.rest.transformer.UserAuthenticationTransformer;
import com.matchandtrade.rest.v1.json.UserAuthenticationJson;

@RestController
@RequestMapping(path = "/rest/v1/authentications")
public class AuthenticationController extends Controller {

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public UserAuthenticationJson get() {
		// Get user authentication
		UserAuthentication userAuthentication = getUserAuthentication();
		// Throw AuthorizationException if there is no UserAuthentication. User is not authenticated.
		if (userAuthentication == null) {
			throw new AuthorizationException(AuthorizationException.Type.UNAUTHORIZED);
		}
		// Transform the response
		UserAuthenticationJson result = UserAuthenticationTransformer.transform(userAuthentication);
		return result;
	}
}
