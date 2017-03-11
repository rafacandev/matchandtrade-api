package com.matchandtrade.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Component;

import com.matchandtrade.authentication.AuthenticationResponseJson;
import com.matchandtrade.config.AuthenticationProperties;
import com.matchandtrade.model.AuthenticationModel;
import com.matchandtrade.model.UserModel;
import com.matchandtrade.persistence.entity.AuthenticationEntity;
import com.matchandtrade.persistence.entity.UserEntity;
import com.matchandtrade.rest.v1.transformer.UserTransformer;
import com.matchandtrade.test.random.UserRandom;

@Component
public class MockFactory {
	
	@Autowired
	private AuthenticationModel authenticationModel;
	@Autowired
	private UserModel userModel;
	@Autowired
	private UserTransformer userTransformer;
	
	public AuthenticationResponseJson nextRandomUserAuthenticationPersisted() {
		UserEntity userEntity = userTransformer.transform(UserRandom.nextJson());
		userModel.save(userEntity);

		AuthenticationResponseJson result = new AuthenticationResponseJson();
		result.setEmail(userEntity.getEmail());
		result.setName(userEntity.getName());
		result.setNewUser(true);
		result.setUserId(userEntity.getUserId());
		
		AuthenticationEntity authenticationEntity = new AuthenticationEntity();
		authenticationEntity.setUserId(userEntity.getUserId());
		authenticationEntity.setToken(userEntity.getUserId().toString());

		authenticationModel.save(authenticationEntity);
		
		return result;
	}

	public MockHttpServletRequest getHttpRequestWithAuthenticatedUser() {
		AuthenticationResponseJson userAuthentication = nextRandomUserAuthenticationPersisted();
		MockHttpServletRequest result = new MockHttpServletRequest();
		result.getSession().setAttribute("user", userAuthentication);
		return result;
	}
	
	public MockHttpServletRequest getHttpRequestWithAuthenticatedUser(AuthenticationResponseJson userAuthentication) {
		MockHttpServletRequest result = new MockHttpServletRequest();
		result.getSession().setAttribute("user", userAuthentication);
		result.addHeader(AuthenticationProperties.OAuth.AUTHORIZATION_HEADER.toString(), userAuthentication.getUserId());
		return result;
	}

	public MockHttpServletRequest getAuthenticatedRequest(AuthenticationEntity authenticationEntity) {
		MockHttpServletRequest result = new MockHttpServletRequest();
		result.addHeader(AuthenticationProperties.OAuth.AUTHORIZATION_HEADER.toString(), authenticationEntity.getToken());
		return result;
	}

	public AuthenticationEntity getAuthentication() {
		UserEntity userEntity = UserRandom.nextEntity();
		userModel.save(userEntity);
		AuthenticationEntity result = getAuthentication(userEntity);
		return result;
	}

	public AuthenticationEntity getAuthentication(UserEntity userEntity) {
		userModel.save(userEntity);
		AuthenticationEntity result = new AuthenticationEntity();
		result.setUserId(userEntity.getUserId());
		result.setToken(userEntity.getUserId() + "-" + userEntity.getName() + "-" + userEntity.getEmail());
		authenticationModel.save(result);
		return result;
	}
	
}
