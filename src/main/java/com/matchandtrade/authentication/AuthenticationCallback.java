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

	protected void authenticate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		AuthenticationResponseJson userInfoFromAuthenticationAuthority = authenticationOAuth.obtainUserInformation(accessToken);
		
		// oAuth Step 6. Authenticate the user
		AuthenticationResponseJson persistedUserInfo = persistAuthentication(antiForgeryState, accessToken, userInfoFromAuthenticationAuthority);
		
		// Using accessToken as AuthorizationToken since authorization is managed locally instead of the Authentication Authority
		// Assign the accessToken to the Authorization header
		response.addHeader(AuthenticationProperties.OAuth.AUTHORIZATION_HEADER.toString(), accessToken);
		// Write the Authorization header to the response body
		response.getWriter().print(generateResponseBody(persistedUserInfo, accessToken));
	}

	private String generateResponseBody(AuthenticationResponseJson authenticationResponseJson, String authorizationHeader) {
		StringBuffer result = new StringBuffer();
		result.append("{");
		result.append("\"userId\": \"").append(authenticationResponseJson.getUserId()).append("\", ");
		result.append("\"email\": \"").append(authenticationResponseJson.getEmail()).append("\", ");
		result.append("\"name\": \"").append(authenticationResponseJson.getName()).append("\", ");
		result.append("\"authorizationHeader\": \"").append(authorizationHeader).append("\"");
		result.append("}");
		return result.toString();
	}

	/*
	 * Persists authentication details
	 */
	private AuthenticationResponseJson persistAuthentication(String stateAttribute, String accessToken, AuthenticationResponseJson user) {
		// Update user information in the local database
		AuthenticationResponseJson result = updateUserInfo(user.getEmail(), user.getName());
		// Put the user in the session
		AuthenticationEntity authenticationEntity = authenticationModel.getByToken(accessToken);
		if (authenticationEntity == null) {
			authenticationEntity = new AuthenticationEntity();
		}
		authenticationEntity.setToken(accessToken);
		authenticationEntity.setUserId(result.getUserId());
		authenticationModel.save(authenticationEntity);
		return result;
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
	private AuthenticationResponseJson updateUserInfo(String email, String name) {
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
		AuthenticationResponseJson result = new AuthenticationResponseJson(
				userEntity.getUserId(), 
				isNewUser, 
				userEntity.getEmail(), 
				userEntity.getName(), 
				null);
		return result;
	}
}
