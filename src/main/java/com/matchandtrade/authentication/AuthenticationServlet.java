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
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.facade.AuthenticationRespositoryFacade;


@WebServlet(name="authenticationServlet", urlPatterns="/authenticate/*")
@Component
public class AuthenticationServlet extends HttpServlet {
	private static final long serialVersionUID = 373664290851751809L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServlet.class);
	
	@Autowired
	private AuthenticationProperties authenticationProperties;
	@Autowired
	private AuthenticationOAuth authenticationOAuth;
	@Autowired
	private AuthenticationCallback authenticationCallbak;
	@Autowired
	private AuthenticationRespositoryFacade authenticationRepository;

	
	/**
	 * Delegates the request to the correct action.
	 * If {@code request.getRequestURI()} ends in 'sign-out' it ends the session.
	 * If {@code request.getRequestURI()} ends in 'authenticate' it redirect to the Authentication Authority.
	 * If {@code request.getRequestURI()} ends in 'callback' it delegates to {@code AuthenticationCallback.authenticate()}.
	 * If {@code request.getRequestURI()} ends in 'info' it delegates to {@code AuthenticationInfoUtil.writeResponseBody()}.
	 * Otherwise returns {@code Response.Status.NOT_FOUND}
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AuthenticationProperties.Action targetAction = obtainAuthenticationAction(request);
		LOGGER.debug("Performing Authentication Action {} for requet [{}].", targetAction, request.getRequestURI());
		try {
			if (targetAction == null) {
				LOGGER.info("URI request not recognized: {}", request.getRequestURI());
				response.setStatus(404);
				return;
			}
			switch (targetAction) {
			case SIGNOUT:
				signOut(request, response);
				break;
			case AUTHENTICATE:
				saveCallbackUrlInSession(request);
				redirectToAuthenticationServer(response);
				break;
			case CALLBACK:
				authenticationCallbak.authenticate(request, response);
				break;
			case INFO:
				AuthenticationInfoUtil.writeResponseBody(request, response);
				break;
			default:
				response.setStatus(Response.Status.NOT_FOUND.getStatusCode());
				break;
			}
		} catch (Exception e) {
			LOGGER.error("Error when performing a GET /authenticate/* with targetAction {}", targetAction, e);
			response.setStatus(500);
		}
	}
	
	private void saveCallbackUrlInSession(HttpServletRequest request) {
		String callbackUrl = request.getParameter(AuthenticationParameter.CALLBACK_URL.toString());
		request.getSession().setAttribute(AuthenticationParameter.CALLBACK_URL.toString(), callbackUrl);
		request.getSession().setMaxInactiveInterval(authenticationProperties.getSessionTimeout());
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
		if (requestUri.lastIndexOf('/') == requestUri.length()-1) {
			requestUri = requestUri.substring(0, requestUri.length()-1);
		}
		
		int lastPathIndex = requestUri.lastIndexOf('/');
		String lastPath = requestUri.substring(lastPathIndex+1);
		result = AuthenticationProperties.Action.get(lastPath);
		return result;
	}

	private void redirectToAuthenticationServer(HttpServletResponse response) throws AuthenticationException {
		// oAuth Step 1. Create an anti-forgery state token
		String state = generateAntiForgeryToken();
		
		// Persist the state token. This will not be in the session as we want a stateless server.
		AuthenticationEntity authenticationEntity = new AuthenticationEntity();
		authenticationEntity.setAntiForgeryState(state);
		authenticationRepository.save(authenticationEntity);

		// oAuth Step 2. Send an authentication request to the Authorization Authority
		authenticationOAuth.redirectToAuthorizationAuthority(response, state, authenticationProperties.getClientId(), authenticationProperties.getRedirectURI());
		LOGGER.debug("Redirecting request to Authorization Authority with redirectURI: [{}].", authenticationProperties.getRedirectURI());
	}
	
	public void setAuthenticationOAuth(AuthenticationOAuth authenticationOAuth) {
		this.authenticationOAuth = authenticationOAuth;
	}
	
	private void signOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
		LOGGER.debug("Signing out from session id: [{}]", request.getSession().getId());
		// Delete authentication details
		String accessToken = request.getHeader(AuthenticationProperties.OAuth.AUTHORIZATION_HEADER.toString());
		if (accessToken != null) {
			AuthenticationEntity authenticationEntity = authenticationRepository.findByToken(accessToken);
			authenticationRepository.delete(authenticationEntity);
		}
		
		// Invalidate the current session (not required, but good practice overall)
		request.getSession().invalidate();
		response.setStatus(Response.Status.RESET_CONTENT.getStatusCode());
	}
}