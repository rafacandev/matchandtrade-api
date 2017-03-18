package com.matchandtrade.rest.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Component;

import com.matchandtrade.config.AuthenticationProperties;
import com.matchandtrade.model.AuthenticationModel;
import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.test.random.UserRandom;

/**
 * <b>Warning</b> do not use <code>@Autowired AuthenticationController</code> in your tests.
 * Controller classes uses HttpServletRequest to handle authentication and 
 * during integration tests it will be required to mock the HttServletRequest; however, as Controllers are singletons, it will
 * change the state of all classes leading to unpredictable test failures (mostly if tests a executed in multi-thread configuration) 
 * 
 * As a result, it is required to create a new instance of the Controller for every test and configure the dependencies manually,
 * this can be achieved through <code>getMockAuthenticationController()</code>. 
 * 
 * @author rafael.santos.bra@gmail.com
 *
 */
@Component
public class MockAuthenticationControllerFactory {

	@Autowired
	AuthenticationModel authenticationModel;
	@Autowired
	UserModel userModel;

	public MockAuthenticationController getMockTradeController() {
		MockAuthenticationController result = new MockAuthenticationController(authenticationModel);

		UserEntity authenticatedUserEntity = UserRandom.nextEntity();
		userModel.save(authenticatedUserEntity);
		
		AuthenticationEntity authenticationEntity = new AuthenticationEntity();
		authenticationEntity.setToken("MockUserControllerFactory - userId: " + authenticatedUserEntity.getUserId());
		authenticationEntity.setUserId(authenticatedUserEntity.getUserId());
		authenticationModel.save(authenticationEntity);
		result.authenticationEntity = authenticationEntity;
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader(AuthenticationProperties.OAuth.AUTHORIZATION_HEADER.toString(), authenticationEntity.getToken());

		result.setHttpServletRequest(request);
		result.authenticatedUserEntity = authenticatedUserEntity;
		return result;
	}
	
	public class MockAuthenticationController extends AuthenticationController {
		public MockAuthenticationController(AuthenticationModel authenticationModel) {
			this.authenticationModel = authenticationModel;
		}
		public UserEntity authenticatedUserEntity;
		public AuthenticationEntity authenticationEntity;
	}
	
}
