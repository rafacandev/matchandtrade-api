package com.matchandtrade.rest.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Component;

import com.matchandtrade.authorization.Authorization;
import com.matchandtrade.config.AuthenticationProperties;
import com.matchandtrade.model.AuthenticationModel;
import com.matchandtrade.model.TradeModel;
import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.transformer.TradeTransformer;
import com.matchandtrade.rest.v1.validator.TradeValidator;
import com.matchandtrade.test.random.UserRandom;

/**
 * <b>Warning</b> do not use <code>@Autowired TradeController</code> in your tests.
 * UserController (as all classes that inherit from {@code Controller}) uses HttpServletRequest to handle authentication.
 * During integration tests it will be required to mock the HttServletRequest; however, as Controllers are singletons, it will
 * change the state of all classes leading to unpredictable test failures (mostly if tests a executed in multi-thread configuration) 
 * 
 * As a result, it is required to create a new instance of the Controller for every test and configure the dependencies manually,
 * this can be achieved through <code>getMockTradeController()</code>. 
 * 
 * @author rafael.santos.bra@gmail.com
 *
 */
@Component
public class MockTradeControllerFactory {

	@Autowired
	Authorization authorization;
	@Autowired
	AuthenticationModel authenticationModel;
	@Autowired
	TradeModel tradeModel;
	@Autowired
	UserModel userModel;
	@Autowired
	TradeTransformer userTranformer;
	@Autowired
	TradeValidator userValidator;

	public MockTradeController getMockTradeController() {
		MockTradeController result = new MockTradeController(authorization, tradeModel, userTranformer, userValidator, authenticationModel);

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
	
	public class MockTradeController extends TradeController {
		public MockTradeController(
				Authorization authorization,
				TradeModel tradeModel,
				TradeTransformer TradeTranformer,
				TradeValidator tradeValidator,
				AuthenticationModel authenticationModel) {
			this.authorization = authorization;
			this.authenticationModel = authenticationModel;
			this.tradeModel = tradeModel;
			this.tradeValidador = tradeValidator;
			this.tradeTransformer = TradeTranformer;
		}
		public UserEntity authenticatedUserEntity;
		public AuthenticationEntity authenticationEntity;
	}
	
}
