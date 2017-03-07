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
		// 3. Confirm anti-forgery state token
		String stateParameter = request.getParameter("state");
		String stateAttribute = (String) request.getSession().getAttribute(AuthenticationProperties.Token.ANTI_FORGERY_STATE.toString());

		logger.debug("Received request with stateParameter: [{}] and authenticationStateAttribute: [{}]", stateParameter, stateAttribute);
		// Return HTTP-STATUS 401 if anti-forgery state token does not match
		if (stateAttribute == null || !stateAttribute.equals(stateParameter)) {
			response.setStatus(401);
			request.getSession().invalidate();
			return;
		}
		
		// 4. Exchange code for access token and ID token
		String accessToken = authenticationOAuth.obtainAccessToken(
				request.getParameter("code"),
				authenticationProperties.getClientId(),
				authenticationProperties.getClientSecret(),
				authenticationProperties.getRedirectURI());
		
		// 5. Obtain user information from the ID token
		UserAuthentication user = authenticationOAuth.obtainUserInformation(accessToken);
		
		// 6. Authenticate the user
		// Update user information in the local database
		user = updateUserInfo(user.getEmail(), user.getName());
		// Put the user in the session
		AuthenticationEntity authenticationEntity = authenticationModel.get(accessToken);
		if (authenticationEntity == null) {
			authenticationEntity = new AuthenticationEntity();
		}
		authenticationEntity.setToken(accessToken);
		authenticationEntity.setAntiForgeryState(stateAttribute);
		authenticationEntity.setUserId(user.getUserId());
		authenticationModel.save(authenticationEntity);
//		user.setAuthenticated(true);
//		request.getSession().setAttribute("user", user);
		
		// Done. Let's redirect the request
		String userStatusPathParam = user.isNewUser() ? "new-user" : "existing-user";
		response.addHeader(AuthenticationProperties.AUTHENTICATION_HEADER, accessToken);
		response.sendRedirect("/webui/#/authentication/" + userStatusPathParam);
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
