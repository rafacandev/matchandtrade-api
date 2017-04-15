package com.matchandtrade.rest.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.authorization.Authorization;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.repository.AuthenticationRespository;
import com.matchandtrade.repository.TradeRepository;
import com.matchandtrade.repository.UserRespository;
import com.matchandtrade.rest.AuthenticationProvider;
import com.matchandtrade.rest.v1.transformer.TradeTransformer;
import com.matchandtrade.rest.v1.transformer.UserTransformer;
import com.matchandtrade.rest.v1.validator.TradeValidator;
import com.matchandtrade.rest.v1.validator.UserValidator;
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
	UserRespository userRepository;
	@Autowired
	AuthenticationRespository authentRepository;
	@Autowired
	Authorization authorization;
	@Autowired
	TradeRepository tradeRepository;
	@Autowired
	TradeValidator tradeValidador;
	@Autowired
	TradeTransformer tradeTransformer;
	@Autowired
	UserValidator userValidador;
	@Autowired
	UserTransformer userTransformer;
	
	private class MockAuthenticationProvider extends AuthenticationProvider {
		public AuthenticationEntity authenticationEntity;
		public MockAuthenticationProvider() {
			UserEntity authenticatedUserEntity = UserRandom.nextEntity();
			userRepository.save(authenticatedUserEntity);
			AuthenticationEntity authenticationEntity = new AuthenticationEntity();
			authenticationEntity.setToken("MocControllerFactory#userId: " + authenticatedUserEntity.getUserId());
			authenticationEntity.setUserId(authenticatedUserEntity.getUserId());
			authentRepository.save(authenticationEntity);
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
		result.tradeRepository = tradeRepository;
		result.tradeTransformer = tradeTransformer;
		result.tradeValidador = tradeValidador;
		result.authenticationProvider = new MockAuthenticationProvider();
		return result;
	}
	
	public UserController getUserController() {
		UserController result = new UserController();
		result.authorization = authorization;
		result.userRepository = userRepository;
		result.userTransformer = userTransformer;
		result.userValidador = userValidador;
		result.authenticationProvider = new MockAuthenticationProvider();
		return result;
	}

}
