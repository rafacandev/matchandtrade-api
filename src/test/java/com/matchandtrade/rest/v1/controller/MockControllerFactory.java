package com.matchandtrade.rest.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.authorization.Authorization;
import com.matchandtrade.model.AuthenticationModel;
import com.matchandtrade.model.TradeModel;
import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.v1.transformer.TradeTransformer;
import com.matchandtrade.rest.v1.validator.TradeValidator;
import com.matchandtrade.test.random.UserRandom;

/**
 * <b>Warning</b> do not use <code>@Autowired *Controller</code> in your tests.
 * Controller classes uses ScopeRequest; so the container will creates an instance per request.
 * Integration tests do run in a container hence treat each controller as singleton.
 * 
 * Treating controllers as singleton may cause testing leakage because they need to
 * access the HttpServletRequest to handle authentication.
 * 
 * As a result, it is required to create a new instance of the {@code *Controller}
 * for every test and configure the dependencies manually,
 * this can be achieved through {@code MockControllerFactory.get*Controller()} 
 * 
 * @author rafael.santos.bra@gmail.com
 *
 */
@Component
public class MockControllerFactory {

	@Autowired
	UserModel userModel;
	@Autowired
	AuthenticationModel authentModel;
	@Autowired
	Authorization authorization;
	@Autowired
	TradeModel tradeModel;
	@Autowired
	TradeValidator tradeValidador;
	@Autowired
	TradeTransformer tradeTransformer;
	
	private class MockAuthenticationProvider extends AuthenticationProvider {
		public AuthenticationEntity authenticationEntity;
		public MockAuthenticationProvider() {
			UserEntity authenticatedUserEntity = UserRandom.nextEntity();
			userModel.save(authenticatedUserEntity);
			AuthenticationEntity authenticationEntity = new AuthenticationEntity();
			authenticationEntity.setToken("MocControllerFactory#userId: " + authenticatedUserEntity.getUserId());
			authenticationEntity.setUserId(authenticatedUserEntity.getUserId());
			authentModel.save(authenticationEntity);
			this.authenticationEntity = authenticationEntity;
		}
		@Override
		public AuthenticationEntity getAuthentication() {
			return authenticationEntity;
		}
	}

	public AuthenticationController getAuthenticationController() {
		AuthenticationController result = new AuthenticationController();
		result.authenticationProvider = new MockAuthenticationProvider();
		return result;
	}

	public TradeController getTradeController() {
		TradeController result = new TradeController();
		result.authorization = authorization;
		result.tradeModel = tradeModel;
		result.tradeTransformer = tradeTransformer;
		result.tradeValidador = tradeValidador;
		result.authenticationProvider = new MockAuthenticationProvider();
		return result;
	}

}
