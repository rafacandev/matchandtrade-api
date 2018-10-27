package com.matchandtrade.authentication;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.config.AuthenticationProperties;
import com.matchandtrade.config.MatchAndTradePropertyKeys;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.AuthenticationRespositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;

@Component
public class AuthenticationCallback {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationCallback.class);
	
	@Autowired
	protected AuthenticationProperties authenticationProperties;
	@Autowired
	protected AuthenticationOAuth authenticationOAuth;
	@Autowired
	protected UserRepositoryFacade userRepository;
	@Autowired
	protected AuthenticationRespositoryFacade authenticationRepository;

	@Transactional
	protected void authenticate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// oAuth Step 3. Confirm anti-forgery state token
		String stateParameter = request.getParameter(AuthenticationProperties.OAuth.STATE_PARAMETER.toString());
		LOGGER.debug("Received request with state parameter: [{}]", stateParameter);

		AuthenticationEntity authenticationEntity = authenticationRepository.findByAtiForgeryState(stateParameter);
		// Return HTTP-STATUS 401 if anti-forgery state token is not found
		if (authenticationEntity == null) {
			LOGGER.debug("Invalidating session because there is no authentication for state parameter: [{}].", stateParameter);
			response.setStatus(401);
			request.getSession().invalidate();
			return;
		}
		
		// oAuth Step 4. Exchange code for access token and ID token
		String accessToken = authenticationOAuth.obtainAccessToken(
				request.getParameter(AuthenticationProperties.OAuth.CODE_PARAMETER.toString()),
				authenticationProperties.getClientId(),
				authenticationProperties.getClientSecret(),
				authenticationProperties.getRedirectURI());
		
		// oAuth Step 5. Obtain user information from the ID token
		LOGGER.debug("Obtaining user information by access token.");
		AuthenticationResponsePojo userInfoFromAuthenticationAuthority = authenticationOAuth.obtainUserInformation(accessToken);
		
		// oAuth Step 6. Authenticate the user
		LOGGER.debug("Authenticating user with email: {}", userInfoFromAuthenticationAuthority.getEmail());
		AuthenticationResponsePojo persistedUserInfo = updateUserInfo(userInfoFromAuthenticationAuthority.getEmail(), userInfoFromAuthenticationAuthority.getName());
		LOGGER.debug("Storing authentication for userId: {}", persistedUserInfo.getUserId());
		updateAuthenticationInfo(authenticationEntity, persistedUserInfo.getUserId(), accessToken);
		
		// Using accessToken as AuthorizationToken since authorization is managed locally instead of the Authentication Authority
		response.addHeader(AuthenticationProperties.OAuth.AUTHORIZATION_HEADER.toString(), accessToken);
		LOGGER.debug("Setting authentication in session for userId: {}", persistedUserInfo.getUserId());
		request.getSession().setMaxInactiveInterval(authenticationProperties.getSessionTimeout());
		request.getSession().setAttribute(AuthenticationProperties.OAuth.AUTHORIZATION_HEADER.toString(), accessToken);
		
		// Redirect the response
		redirectResponse(request, response);
	}

	/**
	 * Redirects the response according to <code>request.getSession().getAttribute("callbackUrl")</code>
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void redirectResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String callbackUrl = authenticationProperties.getCallbackUrl();
		LOGGER.debug("Redirecting request to callback url property {} with value: {}", MatchAndTradePropertyKeys.AUTHENTICATION_CLIENT_CALLBACK_URL, callbackUrl);
		response.sendRedirect(callbackUrl);
	}

	/*
	 * Update authentication details
	 */
	private void updateAuthenticationInfo(
			AuthenticationEntity authenticationEntity,
			Integer userId,
			String accessToken) {
		// Persists Authentication info
		authenticationEntity.setUser(userRepository.get(userId));
		authenticationEntity.setAntiForgeryState(null);
		authenticationEntity.setToken(accessToken);
		authenticationRepository.save(authenticationEntity);
	}

	/**
	 * Check if there is a user for the given email.
	 * If not, then save a new user in the local database.
	 * If yes, then return the information from the local database as the exiting user.
	 * 
	 * @return updated User.
	 */
	private AuthenticationResponsePojo updateUserInfo(String email, String name) {
		UserEntity userEntity = userRepository.findByEmail(email);
		boolean isNewUser = false;
		if (userEntity == null) {
			userEntity = new UserEntity();
			userEntity.setEmail(email);
			userEntity.setName(name);
			userRepository.save(userEntity);
			isNewUser = true;
		}
		return new AuthenticationResponsePojo(
				userEntity.getUserId(), 
				isNewUser, 
				userEntity.getEmail(), 
				userEntity.getName(), 
				null);
	}
}
