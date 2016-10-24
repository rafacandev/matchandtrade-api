package com.matchandtrade.rest.v1.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authentication.UserAuthentication;
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
		// Transform the response
		UserAuthenticationJson result = UserAuthenticationTransformer.transform(userAuthentication);
		return result;
	}
}
