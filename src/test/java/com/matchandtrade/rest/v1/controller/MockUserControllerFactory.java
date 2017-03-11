package com.matchandtrade.rest.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Component;

import com.matchandtrade.authorization.Authorization;
import com.matchandtrade.config.AuthenticationProperties;
import com.matchandtrade.model.AuthenticationModel;
import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.transformer.UserTransformer;
import com.matchandtrade.rest.v1.validator.UserValidator;
import com.matchandtrade.test.random.UserRandom;

/**
 * <b>Warning</b> do not use <code>@Autowired UserController</code> in your tests.
 * UserController (as all classes that inherit from {@code Controller}) uses HttpServletRequest to handle authentication.
 * During integration tests it will be required to mock the HttServletRequest; however, as Controllers are singletons, it will
 * change the state of all classes leading to unpredictable test failures (mostly if tests a executed in multi-thread configuration) 
 * 
 * As a result, it is required to create a new instance of the Controller for every test and configure the dependencies manually,
 * this can be achieved through <code>getMockUserController()</code>. 
 * 
 * @author rafael.santos.bra@gmail.com
 *
 */
@Component
public class MockUserControllerFactory {

	@Autowired
	Authorization authorization;
	@Autowired
	AuthenticationModel authenticationModel;
	@Autowired
	UserModel userModel;
	@Autowired
	UserTransformer userTranformer;
	@Autowired
	UserValidator userValidator;

	public MockUserController getMockUserController() {
		UserEntity authenticatedUserEntity = UserRandom.nextEntity();
		userModel.save(authenticatedUserEntity);
		
		AuthenticationEntity authenticationEntity = new AuthenticationEntity();
		authenticationEntity.setToken("MockUserControllerFactory - userId: " + authenticatedUserEntity.getUserId());
		authenticationEntity.setUserId(authenticatedUserEntity.getUserId());
		authenticationModel.save(authenticationEntity);
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader(AuthenticationProperties.OAuth.AUTHORIZATION_HEADER.toString(), authenticationEntity.getToken());

		MockUserController result = new MockUserController(authorization, userModel, userTranformer, userValidator, authenticationModel);
		result.setHttpServletRequest(request);
		result.userEntity = authenticatedUserEntity;
		return result;
	}
	
	public class MockUserController extends UserController {
		public MockUserController(Authorization authorization, UserModel userModel, UserTransformer userTranformer, UserValidator userValidator, AuthenticationModel authenticationModel) {
			this.authorization = authorization;
			this.userModel = userModel;
			this.userTransformer = userTranformer;
			this.userValidador = userValidator;
			this.authenticationModel = authenticationModel;
		}
		public UserEntity userEntity;
	}
	
}
