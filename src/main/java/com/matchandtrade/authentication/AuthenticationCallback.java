package com.matchandtrade.authentication;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.matchandtrade.config.AuthenticationProperties;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.persistence.facade.AuthenticationRespositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;

@Component
public class AuthenticationCallback implements Serializable {

	private static final long serialVersionUID = 6742828244957421933L;
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationCallback.class);
	
	@Autowired
	private AuthenticationProperties authenticationProperties;
	@Autowired
	private AuthenticationOAuth authenticationOAuth;
	@Autowired
	private UserRepositoryFacade userRepository;
	@Autowired
	private AuthenticationRespositoryFacade authenticationRepository;

	@Transactional
	protected void authenticate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// oAuth Step 3. Confirm anti-forgery state token
		String stateParameter = request.getParameter(AuthenticationProperties.OAuth.STATE_PARAMETER.toString());
		logger.debug("Received request with state parameter: [{}]", stateParameter);

		AuthenticationEntity authenticationEntity = authenticationRepository.getByAtiForgeryState(stateParameter);
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
		AuthenticationResponsePojo userInfoFromAuthenticationAuthority = authenticationOAuth.obtainUserInformation(accessToken);
		
		// oAuth Step 6. Authenticate the user
		AuthenticationResponsePojo persistedUserInfo = updateUserInfo(userInfoFromAuthenticationAuthority.getEmail(), userInfoFromAuthenticationAuthority.getName());
		updateAuthenticationInfo(authenticationEntity, persistedUserInfo.getUserId(), accessToken);
		
		/*
		 * Using accessToken as AuthorizationToken since authorization is managed locally instead of the Authentication Authority
		 * Assign the accessToken to the Authorization header
		 * Returns only the Authorization header, at the moment there is no need for a response body
		 */
		response.addHeader(AuthenticationProperties.OAuth.AUTHORIZATION_HEADER.toString(), accessToken);
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
		// TODO remove this when refactor authenticationRepository.getByAtiForgeryState(). We are getting org.hibernate.PersistentObjectException: detached entity passed to persist: com.matchandtrade.persistence.entity.AuthenticationEntity
		AuthenticationEntity authenticationEntity2 = authenticationRepository.get(authenticationEntity.getAuthenticationId());
		authenticationEntity2.setUser(userRepository.get(userId));
		authenticationEntity2.setAntiForgeryState(null);
		authenticationEntity2.setToken(accessToken);
		authenticationRepository.save(authenticationEntity2);
		// TODO remove this when refactor authenticationRepository.getByAtiForgeryState(). We are getting org.hibernate.PersistentObjectException: detached entity passed to persist: com.matchandtrade.persistence.entity.AuthenticationEntity
		authenticationEntity = authenticationEntity2;
	}

	/**
	 * Check if there is a user for the given email.
	 * If not, then save a new user in the local database.
	 * If yes, then return the information from the local database as the exiting user.
	 * 
	 * @return updated User.
	 */
	private AuthenticationResponsePojo updateUserInfo(String email, String name) {
		UserEntity userEntity = userRepository.getByEmail(email);
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
