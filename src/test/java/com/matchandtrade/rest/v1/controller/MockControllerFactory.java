package com.matchandtrade.rest.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.model.AuthenticationModel;
import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.AuthenticationProvider;
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
public class MockControllerFactory {

	@Autowired
	AuthenticationModel authenticationModel;
	@Autowired
	UserModel userModel;

	
	public class MockAuthenticationController extends AuthenticationController {
		public MockAuthenticationController(AuthenticationModel authenticationModel) {
			this.authenticationModel = authenticationModel;
		}
		public AuthenticationModel authenticationModel;
		public UserEntity authenticatedUserEntity;
		public AuthenticationEntity authenticationEntity;
	}
	
	public AuthenticationController getAuthenticationController() {
		UserEntity authenticatedUserEntity = UserRandom.nextEntity();
		userModel.save(authenticatedUserEntity);
		AuthenticationEntity authenticationEntity = new AuthenticationEntity();
		authenticationEntity.setToken("MocControllerFactory#userId: " + authenticatedUserEntity.getUserId());
		authenticationEntity.setUserId(authenticatedUserEntity.getUserId());
		authenticationModel.save(authenticationEntity);
		
		
		AuthenticationController result = new AuthenticationController();
		result.authenticationProvider = new MockAuthenticationProvider(authenticationEntity);
		return result;
	}
	
	public class MockAuthenticationProvider extends AuthenticationProvider {
		public AuthenticationEntity authenticationEntity;
		public MockAuthenticationProvider(AuthenticationEntity authenticationEntity) {
			this.authenticationEntity = authenticationEntity;
		}
		@Override
		public AuthenticationEntity getAuthentication() {
			return authenticationEntity;
		}
	}
	
}
