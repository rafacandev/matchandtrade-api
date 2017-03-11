package com.matchandtrade.authentication;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.config.AuthenticationProperties;
import com.matchandtrade.model.AuthenticationModel;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.rest.v1.controller.UserController;
import com.matchandtrade.rest.v1.json.UserJson;
import com.matchandtrade.test.TestingDefaultAnnotations;

@RunWith(SpringRunner.class)
@TestingDefaultAnnotations
public class AuthenticationIT {

	@Autowired
	private AuthenticationServlet authenticationServlet;
	@Autowired
	private AuthenticationCallback authenticationCallback;
	@Autowired
	private UserController userController;
	@Autowired
	private AuthenticationModel authenticationModel;
	
	@Test
	public void authenticationPositive() throws Exception {
		// Mock request/response for AuthenticationServlet
		MockHttpServletRequest requestAuthentication = new MockHttpServletRequest();
		MockHttpServletResponse responseAuthentication = new MockHttpServletResponse();
		requestAuthentication.setRequestURI("http://localhost:8080/authenticate");

		// Make request to /authenticate
		authenticationServlet.doGet(requestAuthentication, responseAuthentication);
		
		// Mock request/response for AuthenticationCallback
		MockHttpServletRequest requestCallback = new MockHttpServletRequest();
		String state = (String) requestAuthentication.getSession().getAttribute(AuthenticationProperties.OAuth.ANTI_FORGERY_STATE.toString());
		requestCallback.addParameter(AuthenticationProperties.OAuth.STATE_PARAMETER.toString(), state);
		requestCallback.getSession().setAttribute(AuthenticationProperties.OAuth.ANTI_FORGERY_STATE.toString(), state);
		
		// Make request to AuthenticationCallback
		MockHttpServletResponse responseCallback = new MockHttpServletResponse();
		authenticationCallback.authenticate(requestCallback, responseCallback);

		// Mock request/response for UserController
		MockHttpServletRequest requestUserController = new MockHttpServletRequest();
		String authenticationHeader = responseCallback.getHeader(AuthenticationProperties.OAuth.AUTHORIZATION_HEADER.toString());
		requestUserController.addHeader(AuthenticationProperties.OAuth.AUTHORIZATION_HEADER.toString(), authenticationHeader);
		
		AuthenticationEntity authenticationEntity = authenticationModel.getByToken(authenticationHeader);
		userController.setHttpServletRequest(requestUserController);

		// Make request to UserController
		UserJson userControllerResponse = userController.get(authenticationEntity.getUserId());
		assertNotNull(userControllerResponse);
	}

}
