package com.matchandtrade.rest.v1.controller;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.service.AuthenticationService;
import com.matchandtrade.rest.service.UserService;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.rest.v1.transformer.UserTransformer;
import com.matchandtrade.rest.v1.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/matchandtrade-api/v1/users")
public class UserController implements Controller {

	@Autowired
	AuthenticationService authenticationService;
	@Autowired
	UserService userService;
	private UserTransformer userTransformer = new UserTransformer();
	@Autowired
	UserValidator userValidador;

	@RequestMapping(path="/{userId}", method=RequestMethod.GET)
	public UserJson get(@PathVariable("userId") Integer userId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationService.findCurrentAuthentication());
		// Validate the request - nothing to validate
		// Delegate to service
		UserEntity userEntity = userService.findByUserId(userId);
		UserEntity sanitizedUser = userService.sanitize(authenticationService.findCurrentAuthentication().getUser(), userEntity);
		// Transform the response
		UserJson response = userTransformer.transform(sanitizedUser);
		// TODO: Assemble links
		return response;
	}
	
	@RequestMapping(path="/{userId}", method=RequestMethod.PUT)
	public UserJson put(@PathVariable("userId") Integer userId, @RequestBody UserJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationService.findCurrentAuthentication());
		// Validate the request
		requestJson.setUserId(userId); // Always get the id from the URL when working on PUT methods
		userValidador.validatePut(authenticationService.findCurrentAuthentication().getUser(), requestJson);
		// Transform the request
		UserEntity userEntity = userTransformer.transform(requestJson);
		// Delegate to Service layer
		userService.update(userEntity);
		// Transform the response
		UserJson response = userTransformer.transform(userEntity);
		// TODO: Assemble links
		return response;
	}

}
