package com.matchandtrade.authentication;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import com.matchandtrade.authorization.AuthorizationException;
import com.matchandtrade.config.AuthenticationProperties;
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
	
	@Test
	public void authenticationPositive() throws ServletException, IOException {
		MockHttpServletRequest requestAuthentication = new MockHttpServletRequest();
		MockHttpServletResponse responseAuthentication = new MockHttpServletResponse();
		requestAuthentication.setRequestURI("http://localhost:8080/authenticate");
		authenticationServlet.doGet(requestAuthentication, responseAuthentication);

		MockHttpServletRequest requestCallback = new MockHttpServletRequest();
		requestCallback.setParameter("state", "testing-state");
		requestCallback.getSession().setAttribute(AuthenticationProperties.Token.ANTI_FORGERY_STATE.toString(), "testing-state");
		MockHttpServletResponse responseCallback = new MockHttpServletResponse();
		authenticationCallback.doGet(requestCallback, responseCallback);
		
		UserAuthentication userAuthentication = (UserAuthentication) requestCallback.getSession().getAttribute("user");
		userController.setHttpServletRequest(requestCallback);
		UserJson userControllerResponse = userController.get(userAuthentication.getUserId());
		assertNotNull(userControllerResponse);
	}
	
	@Test(expected=AuthorizationException.class)
	public void authenticationNegative() throws ServletException, IOException {
		MockHttpServletRequest requestAuthentication = new MockHttpServletRequest();
		MockHttpServletResponse responseAuthentication = new MockHttpServletResponse();
		requestAuthentication.setRequestURI("http://localhost:8080/authenticate");
		authenticationServlet.doGet(requestAuthentication, responseAuthentication);

		MockHttpServletRequest requestCallback = new MockHttpServletRequest();
		requestCallback.setParameter("state", "testing-state");
		requestCallback.getSession().setAttribute(AuthenticationProperties.Token.ANTI_FORGERY_STATE.toString(), "testing-state");
		MockHttpServletResponse responseCallback = new MockHttpServletResponse();
		authenticationCallback.doGet(requestCallback, responseCallback);
		
		UserAuthentication userAuthentication = (UserAuthentication) requestCallback.getSession().getAttribute("user");
		userController.setHttpServletRequest(requestCallback);
		userController.get(userAuthentication.getUserId()+1);
	}
	
}
