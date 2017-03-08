package com.matchandtrade.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Component;

import com.matchandtrade.authentication.UserAuthentication;
import com.matchandtrade.model.AuthenticationModel;
import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.transformer.UserTransformer;
import com.matchandtrade.test.random.UserRandom;

@Component
public class MockFactory {
	
	public enum AuthenticationValue {
		ANTI_FORGERY_STATE, TOKEN
	}
	
	@Autowired
	private AuthenticationModel authenticationModel;
	@Autowired
	private UserModel userModel;
	@Autowired
	private UserTransformer userTransformer;
	
	public UserAuthentication nextRandomUserAuthentication() {
		UserEntity userEntity = userTransformer.transform(UserRandom.next());
		UserAuthentication result = new UserAuthentication();
		result.setAuthenticated(true);
		result.setEmail(userEntity.getEmail());
		result.setName(userEntity.getName());
		result.setNewUser(true);
		result.setUserId(userEntity.getUserId());
		return result;
	}
	
	public UserAuthentication nextRandomUserAuthenticationPersisted() {
		UserEntity userEntity = userTransformer.transform(UserRandom.next());
		userModel.save(userEntity);

		UserAuthentication result = new UserAuthentication();
		result.setAuthenticated(true);
		result.setEmail(userEntity.getEmail());
		result.setName(userEntity.getName());
		result.setNewUser(true);
		result.setUserId(userEntity.getUserId());
		return result;
	}

	public MockHttpServletRequest getHttpRequestWithAuthenticatedUser() {
		UserAuthentication userAuthentication = nextRandomUserAuthenticationPersisted();
		MockHttpServletRequest result = new MockHttpServletRequest();
		result.getSession().setAttribute("user", userAuthentication);
		return result;
	}
	
	public MockHttpServletRequest getHttpRequestWithAuthenticatedUser(UserAuthentication userAuthentication) {
		MockHttpServletRequest result = new MockHttpServletRequest();
		result.getSession().setAttribute("user", userAuthentication);
		return result;
	}
	
}
