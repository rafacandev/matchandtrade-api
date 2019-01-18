package com.matchandtrade.authentication;

import com.matchandtrade.config.AppConfigurationProperties;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.facade.AuthenticationRepositoryFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.security.SecureRandom;

@WebServlet(urlPatterns="/matchandtrade-api/v1/authenticate/*")
@Component
public class AuthenticationServlet extends HttpServlet {
	private static final long serialVersionUID = 373664290851751809L;
	
	private final Logger log = LoggerFactory.getLogger(AuthenticationServlet.class);
	
	@Autowired
	AuthenticationOAuth authenticationOAuth;
	@Autowired
	AuthenticationCallback authenticationCallbak;
	@Autowired
	AuthenticationRepositoryFacade authenticationRepository;
	@Autowired
	AppConfigurationProperties configProperties;

	/**
	 * Delegates the request to the correct action.
	 * If {@code request.getRequestURI()} ends in 'sign-out' it ends the session.
	 * If {@code request.getRequestURI()} ends in 'authenticate' it redirect to the Authentication Authority.
	 * If {@code request.getRequestURI()} ends in 'callback' it delegates to {@code AuthenticationCallback.authenticate()}.
	 * If {@code request.getRequestURI()} ends in 'info' it delegates to {@code AuthenticationInfoUtil.writeResponseBody()}.
	 * Otherwise returns {@code Response.Status.NOT_FOUND}
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		AuthenticationAction targetAction = obtainAuthenticationAction(request);
		log.debug("Performing Authentication Action {} for requet [{}].", targetAction, request.getRequestURI());
		try {
			if (targetAction == null) {
				log.info("URI request not recognized: {}", request.getRequestURI());
				response.setStatus(404);
				return;
			}
			switch (targetAction) {
			case SIGN_OFF:
				signOff(request, response);
				break;
			case AUTHENTICATE:
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
			log.error("Error when performing a GET /authenticate/* with targetAction {}", targetAction, e);
			response.setStatus(500);
		}
	}
	
	private String generateAntiForgeryToken() {
	  return new BigInteger(130, new SecureRandom()).toString(32);
	}

	/**
	 * Returns the corresponding action for this request
	 */
	AuthenticationAction obtainAuthenticationAction(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		// Remove tailing slash if any
		if (requestUri.lastIndexOf('/') == requestUri.length()-1) {
			requestUri = requestUri.substring(0, requestUri.length()-1);
		}

		int lastPathIndex = requestUri.lastIndexOf('/');
		String lastPath = requestUri.substring(lastPathIndex+1);
		return AuthenticationAction.fromAlias(lastPath);
	}

	private void redirectToAuthenticationServer(HttpServletResponse response) throws AuthenticationException {
		// oAuth Step 1. Create an anti-forgery state token
		String state = generateAntiForgeryToken();
		
		// Persist the state token. This will not be in the session as we want a stateless server.
		AuthenticationEntity authenticationEntity = new AuthenticationEntity();
		authenticationEntity.setAntiForgeryState(state);
		authenticationRepository.save(authenticationEntity);

		// oAuth Step 2. Send an authentication request to the Authorization Authority
		authenticationOAuth.redirectToAuthorizationAuthority(response, state, configProperties.authentication.getClientId(), configProperties.authentication.getRedirectUrl());
		log.debug("Redirecting request to Authorization Authority with redirectURI: [{}].", configProperties.authentication.getRedirectUrl());
	}
	
	private void signOff(HttpServletRequest request, HttpServletResponse response) {
		log.debug("Signing off from session id: [{}]", request.getSession().getId());
		// Delete authentication details
		String accessToken = request.getHeader(AuthenticationOAuth.AUTHORIZATION_HEADER);
		if (accessToken != null) {
			AuthenticationEntity authenticationEntity = authenticationRepository.findByToken(accessToken);
			if (authenticationEntity != null) {
				authenticationRepository.delete(authenticationEntity);
			}
		}
		
		// Invalidate the current session (not required, but good practice overall)
		request.getSession().invalidate();
		response.setStatus(Response.Status.RESET_CONTENT.getStatusCode());
	}
}