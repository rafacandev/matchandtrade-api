package com.matchandtrade.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.config.AuthenticationProperties;
import com.matchandtrade.model.AuthenticationModel;
import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;

@Component
public class AuthenticationCallback {
	
	private final Logger logger = LoggerFactory.getLogger(AuthenticationCallback.class);
	
	@Autowired
	private AuthenticationProperties authenticationProperties;
	@Autowired
	private AuthenticationOAuth authenticationOAuth;
	@Autowired
	private UserModel userModel;
	@Autowired
	private AuthenticationModel authenticationModel;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// oAuth Step 3. Confirm anti-forgery state token
		String stateParameter = request.getParameter(AuthenticationProperties.OAuth.STATE_PARAMETER.toString());
		String antiForgeryState = (String) request.getSession().getAttribute(AuthenticationProperties.OAuth.ANTI_FORGERY_STATE.toString());
		logger.debug("Received request with stateParameter: [{}] and authenticationStateAttribute: [{}]", stateParameter, antiForgeryState);
		
		// Return HTTP-STATUS 401 if anti-forgery state token does not match
		if (antiForgeryState == null || !antiForgeryState.equals(stateParameter)) {
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
		UserAuthentication user = authenticationOAuth.obtainUserInformation(accessToken);
		
		// oAuth Step 6. Authenticate the user
		persistAuthentication(antiForgeryState, accessToken, user);
		
		// Done. Add authentication header to response
		response.addHeader(AuthenticationProperties.AUTHENTICATION_HEADER, accessToken);
	}

	/*
	 * Persists authentication details
	 */
	private void persistAuthentication(String stateAttribute, String accessToken, UserAuthentication user) {
		// Update user information in the local database
		user = updateUserInfo(user.getEmail(), user.getName());
		// Put the user in the session
		AuthenticationEntity authenticationEntity = authenticationModel.getByToken(accessToken);
		if (authenticationEntity == null) {
			authenticationEntity = new AuthenticationEntity();
		}
		authenticationEntity.setToken(accessToken);
		authenticationEntity.setUserId(user.getUserId());
		authenticationModel.save(authenticationEntity);
		user.setAuthenticated(true);
	}

	/**
	 * Sets the AuthenticationOAuth
	 * @param authenticationOAuth
	 */
	public void setAuthenticationOAuth(AuthenticationOAuth authenticationOAuth) {
		this.authenticationOAuth = authenticationOAuth;
	}

	/**
	 * Check if there is a user for the given email.
	 * If not, then save a new user in the local database.
	 * If yes, then return the information from the local database as the exiting user.
	 * 
	 * @return updated User.
	 */
	private UserAuthentication updateUserInfo(String email, String name) {
		UserEntity userEntity = userModel.get(email);
		boolean isNewUser = false;
		if (userEntity == null) {
			userEntity = new UserEntity();
			userEntity.setEmail(email);
			userEntity.setName(name);
			userEntity.setRole(UserEntity.Role.USER);
			userModel.save(userEntity);
			isNewUser = true;
		}
		UserAuthentication result = new UserAuthentication();
		result.setUserId(userEntity.getUserId());
		result.setEmail(userEntity.getEmail());
		result.setNewUser(isNewUser);
		result.setName(userEntity.getName());
		return result;
	}
}
