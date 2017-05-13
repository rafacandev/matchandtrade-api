package com.matchandtrade.rest.v1.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.matchandtrade.authorization.AuthorizationValidator;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.service.UserService;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.rest.v1.link.UserLinkAssember;
import com.matchandtrade.rest.v1.transformer.UserTransformer;
import com.matchandtrade.rest.v1.validator.UserValidator;

@RestController
@RequestMapping(path="/rest/v1/users/")
public class UserController {

	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	UserValidator userValidador;
	@Autowired
	UserTransformer userTransformer;
	@Autowired
	UserService userService;

	@RequestMapping(path="{userId}", method=RequestMethod.GET)
	public UserJson get(@PathVariable("userId") Integer userId) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		userValidador.validateGetById(authenticationProvider.getAuthentication(), userId);
		// Delegate to Repository layer
		UserEntity userEntity = userService.get(userId);
		// Transform the response
		UserJson response = UserTransformer.transform(userEntity);
		// Assemble links
		UserLinkAssember.assemble(response);
		return response;
	}
	
	@RequestMapping(path="{userId}", method=RequestMethod.PUT)
	public UserJson put(@PathVariable("userId") Integer userId, @RequestBody UserJson requestJson) {
		// Validate request identity
		AuthorizationValidator.validateIdentity(authenticationProvider.getAuthentication());
		// Validate the request
		requestJson.setUserId(userId);
		userValidador.validatePut(requestJson);
		// Transform the request
		UserEntity userEntity = userTransformer.transform(requestJson, true);
		// Delegate to Service layer
		userService.update(userEntity);
		// Transform the response
		UserJson response = UserTransformer.transform(userEntity);
		// Assemble links
		UserLinkAssember.assemble(response);
		return response;
	}
}
