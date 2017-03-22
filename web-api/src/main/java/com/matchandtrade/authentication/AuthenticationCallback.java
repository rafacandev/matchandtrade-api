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
		logger.debug("Received request with state parameter: [{}]", stateParameter);

		AuthenticationEntity authenticationEntity = authenticationModel.getByAtiForgeryState(stateParameter);
		// Return HTTP-STATUS 401 if anti-forgery state token is not found
		if (authenticationEntity == null) {
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
		AuthenticationResponseJson persistedUserInfo = updateUserInfo(userInfoFromAuthenticationAuthority.getEmail(), userInfoFromAuthenticationAuthority.getName());
		updateAuthenticationInfo(authenticationEntity, persistedUserInfo.getUserId(), accessToken);
		
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

	/**
	 * Sets the AuthenticationOAuth
	 * @param authenticationOAuth
	 */
	public void setAuthenticationOAuth(AuthenticationOAuth authenticationOAuth) {
		this.authenticationOAuth = authenticationOAuth;
	}

	/*
	 * Update authentication details
	 */
	private void updateAuthenticationInfo(
			AuthenticationEntity authenticationEntity,
			Integer userId,
			String accessToken) {
		// Persists Authentication info
		authenticationEntity.setUserId(userId);
		authenticationEntity.setAntiForgeryState(null);
		authenticationEntity.setToken(accessToken);
		authenticationModel.save(authenticationEntity);
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
