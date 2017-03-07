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
import com.matchandtrade.model.AuthenticationModel;
import com.matchandtrade.persistence.entity.AuthenticationEntity;


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
	private AuthenticationCallback authenticationCallbakServlet;
	@Autowired
	private AuthenticationModel authenticationModel;

	
	/**
	 * Delegates the request to the correct action.
	 * If request.getRequestURI() ends in 'sign-out' it ends the session.
	 * Otherwise proceeds with the regular user authentication process.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AuthenticationAction targetAction = getAuthenticationAction(request);
		logger.debug("Performing Authentication Action {} for requet [{}].", targetAction, request.getRequestURI());
		if (targetAction == null) {
			response.setStatus(Response.Status.NOT_FOUND.getStatusCode());
		} else if (targetAction == AuthenticationAction.SIGNOUT) {
			signOut(request, response);
			response.setStatus(Response.Status.RESET_CONTENT.getStatusCode());
		} else if (targetAction == AuthenticationAction.AUTHENTICATE) {
			redirectToAuthenticationServer(request, response);
		} else if (targetAction == AuthenticationAction.CALLBACK) {
			authenticationCallbakServlet.doGet(request, response);
		}
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

		AuthenticationEntity authenticationEntity = new AuthenticationEntity();
		authenticationEntity.setAntiForgeryState(state);
		authenticationModel.save(authenticationEntity);
//		request.getSession().setAttribute(AuthenticationProperties.Token.ANTI_FORGERY_STATE.toString(), state);

		// 2. Send an authentication request to the OAuth server
		authenticationOAuth.redirectToAuthorizationAuthority(response, state, authenticationProperties.getClientId(), authenticationProperties.getRedirectURI());
		logger.debug("Redirecting request to Authorization Authority with redirectURI: [{}].", authenticationProperties.getRedirectURI());
	}
	
	public void setAuthenticationOAuth(AuthenticationOAuth authenticationOAuth) {
		this.authenticationOAuth = authenticationOAuth;
	}
	
	private void signOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("Signing out from session id: [{}]", request.getSession().getId());
		request.getSession().invalidate();
	}
}