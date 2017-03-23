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
		requestCallback.setParameter("state", obtainStateParameter(responseAuthentication.getRedirectedUrl()));
		
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
		assertNotNull(userControllerResponse.getUserId());
		
		// Sign-off
		requestUserController.setRequestURI("http://localhost:8080/sign-out");
		authenticationServlet.doGet(requestUserController, new MockHttpServletResponse());
	}

	private String obtainStateParameter(String redirectedUrl) {
		int begin = redirectedUrl.indexOf("state=");
		redirectedUrl = redirectedUrl.substring(begin);
		int end = redirectedUrl.indexOf("&");
		if (end > 0) {
			redirectedUrl = redirectedUrl.substring(6, end);
		} else {
			redirectedUrl = redirectedUrl.substring(6);
		}
		return redirectedUrl;
	}

}
