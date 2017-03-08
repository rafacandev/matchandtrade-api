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
import com.matchandtrade.test.MockFactory;
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
	@Autowired
	private MockFactory mockFactory;
	
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
		MockHttpServletResponse responseCallback = new MockHttpServletResponse();
		String state = (String) requestAuthentication.getSession().getAttribute(AuthenticationProperties.Token.ANTI_FORGERY_STATE.toString());
		requestCallback.addParameter("state", state);
		requestCallback.getSession().setAttribute(AuthenticationProperties.Token.ANTI_FORGERY_STATE.toString(), state);
		
		// Make request to AuthenticationCallback
		authenticationCallback.doGet(requestCallback, responseCallback);

		// Mock request/response for UserController
		MockHttpServletRequest requestUserController = new MockHttpServletRequest();
		String authenticationHeader = responseCallback.getHeader(AuthenticationProperties.AUTHENTICATION_HEADER);
		requestUserController.addHeader(AuthenticationProperties.AUTHENTICATION_HEADER, authenticationHeader);
		AuthenticationEntity authenticationEntity = authenticationModel.getByToken(authenticationHeader);
		userController.setHttpServletRequest(requestUserController);

		// Make request to UserController
		UserJson userControllerResponse = userController.get(authenticationEntity.getUserId());
		assertNotNull(userControllerResponse);
	}

}
