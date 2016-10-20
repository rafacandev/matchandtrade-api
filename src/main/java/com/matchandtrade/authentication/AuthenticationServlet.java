package com.matchandtrade.authentication;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.config.AuthenticationProperties;


@WebServlet(name="authenticationServlet", urlPatterns="/authenticate/*")
@Component
public class AuthenticationServlet extends HttpServlet {
	private static final long serialVersionUID = 373664290851751809L;
	
	private final Logger logger = LoggerFactory.getLogger(AuthenticationServlet.class);
	
	@Autowired
	private AuthenticationProperties authenticationProperties;
	@Autowired
	private AuthenticationOAuth authenticationOAuth;
	@Autowired
	private AuthenticationCallbakServlet authenticationCallbakServlet;
	
	/**
	 * Delegates the request to the correct action.
	 * If request.getRequestURI() ends in 'sing-out' it ends the session.
	 * Otherwise proceeds with the regular user authentication process.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AuthenticationAction targetAction = getAuthenticationAction(request);
		if (targetAction == null) {
			response.setStatus(Response.Status.NOT_FOUND.getStatusCode());
		} else if (targetAction == AuthenticationAction.SIGNOUT) {
			singOut(request, response);
		} else if (targetAction == AuthenticationAction.AUTHENTICATE) {
			redirectToAuthenticationServer(request, response);
		} else if (targetAction == AuthenticationAction.CALLBACK) {
			authenticationCallbakServlet.doGet(request, response);
		}
		logger.debug("Authentication Action not found for URL {}.", request.getRequestURI());
		response.setStatus(Response.Status.NOT_FOUND.getStatusCode());
	}

	private String generateAntiForgeryToken() {
	  return new BigInteger(130, new SecureRandom()).toString(32);
	}

	/**
	 * Returns the corresponding AuthenticationAction for this request
	 */
	AuthenticationAction getAuthenticationAction(HttpServletRequest request) {
		AuthenticationAction result = null;
		String requestUri = request.getRequestURI();
		// Remove tailing slash if any
		if (requestUri.lastIndexOf("/") == requestUri.length()-1) {
			requestUri = requestUri.substring(0, requestUri.length()-1);
		}
		
		int lastPathIndex = requestUri.lastIndexOf("/");
		String lastPath = requestUri.substring(lastPathIndex+1);
		result = AuthenticationAction.get(lastPath);
		return result;
	}

	private void redirectToAuthenticationServer(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		// 1. Create an anti-forgery state token
		String state = generateAntiForgeryToken();
		request.getSession().setAttribute(AuthenticationProperties.Token.ANTI_FORGERY_STATE.toString(), state);

		// 2. Send an authentication request to the OAuth server
		authenticationOAuth.redirectToAuthorizationServer(response, state, authenticationProperties.getClientId(), authenticationProperties.getRedirectURI());
	}
	
	private void singOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().invalidate();
		response.setStatus(Response.Status.OK.getStatusCode());
	}
}