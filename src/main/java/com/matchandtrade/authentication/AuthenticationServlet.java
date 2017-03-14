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
	private AuthenticationCallback authenticationCallbak;

	
	/**
	 * Delegates the request to the correct action.
	 * If {@code request.getRequestURI()} ends in 'sign-out' it ends the session.
	 * If {@code request.getRequestURI()} ends in 'authenticate' it redirect to the Authentication Authority.
	 * If {@code request.getRequestURI()} ends in 'callback' it delegates to {@code AuthenticationCallback.authenticate()}.
	 * Otherwise returns {@code Response.Status.NOT_FOUND}
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AuthenticationProperties.Action targetAction = obtainAuthenticationAction(request);
		logger.debug("Performing Authentication Action {} for requet [{}].", targetAction, request.getRequestURI());
		if (targetAction == null) {
			response.setStatus(Response.Status.NOT_FOUND.getStatusCode());
		} else if (targetAction == AuthenticationProperties.Action.SIGNOUT) {
			signOut(request, response);
			response.setStatus(Response.Status.RESET_CONTENT.getStatusCode());
		} else if (targetAction == AuthenticationProperties.Action.AUTHENTICATE) {
			redirectToAuthenticationServer(request, response);
		} else if (targetAction == AuthenticationProperties.Action.CALLBACK) {
			authenticationCallbak.authenticate(request, response);
		}
	}

	private String generateAntiForgeryToken() {
	  return new BigInteger(130, new SecureRandom()).toString(32);
	}

	/**
	 * Returns the corresponding action for this request
	 */
	AuthenticationProperties.Action obtainAuthenticationAction(HttpServletRequest request) {
		AuthenticationProperties.Action result = null;
		String requestUri = request.getRequestURI();
		// Remove tailing slash if any
		if (requestUri.lastIndexOf("/") == requestUri.length()-1) {
			requestUri = requestUri.substring(0, requestUri.length()-1);
		}
		
		int lastPathIndex = requestUri.lastIndexOf("/");
		String lastPath = requestUri.substring(lastPathIndex+1);
		result = AuthenticationProperties.Action.get(lastPath);
		return result;
	}

	private void redirectToAuthenticationServer(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		// oAuth Step 1. Create an anti-forgery state token
		String state = generateAntiForgeryToken();
		// TODO remove ANTI_FORGERY_STATE from session
		request.getSession().setAttribute(AuthenticationProperties.OAuth.ANTI_FORGERY_STATE.toString(), state);

		// oAuth Step 2. Send an authentication request to the Authorization Authority
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